package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.models.ProductQuotation;
import com.subocol.manage.purchase.domain.ports.persistence.InsurerRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.services.SendManualPurchase;
import com.subocol.manage.purchase.domain.services.ValidateProductFinishTimer;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.TYPE_AUTOMATIC;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.TYPE_MANUAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageNoticeMM {

    private final ValidateProductFinishTimer validateProductFinishTimer;
    private final CreateOrder createOrder;
    private final SendManualPurchase sendManualPurchase;
    private final NoticeRepositoryPort noticeRepositoryPort;
    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;
    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    private final InsurerRepositoryPort insurerRepositoryPort;
    private final Integrations integrations;

    @Transactional(rollbackFor = Exception.class)
    public boolean manageNoticeByNoticeId(Long noticeId, Boolean serviceIntegrations) {
        try {
            long startTime = System.currentTimeMillis();

            Set<Order> lsOrdersAutomatic = new HashSet<>();
            Set<Order> lsOrdersManual = new HashSet<>();
            Notice notice = noticeRepositoryPort.findById(noticeId)
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(NOTICE_NOT_FOUND, noticeId)));
            log.info("Start ManageNotice for noticeId: " + noticeId);
            validateProductFinishTimer.updateActiveProductQuotation(notice.getId());

            if (notice.isAuth()) {

                List<ProductQuotation> listProductQuotation = validateProductFinishTimer.findProductsQuotationByPriorityOne(notice.getId(), notice.getExternalEvent().toString());

                List<Long> listIdsProductQuotation = listProductQuotation.stream().map(ProductQuotation::getId).toList();

                validateProductFinishTimer.manageStatusProductQuotation(notice.getId(), notice.getExternalEvent().toString(), listIdsProductQuotation, notice.isAuth());

                validateProductFinishTimer.manageStatusQuotation(notice.getId());

                if (!listProductQuotation.isEmpty()) {

                    lsOrdersAutomatic = createOrder.createOrderQuotation(notice, listProductQuotation, TYPE_AUTOMATIC);

                    productQuotationRepositoryPort.updatePurchaseProductQuotation(listIdsProductQuotation, true);
                }

                List<ProductQuotation> lsProductQuotationManual = productQuotationRepositoryPort.findAllWinnersByNotice(notice.getId());

                if (!lsProductQuotationManual.isEmpty()) {

                    lsOrdersManual = createOrder.createOrderQuotation(notice, lsProductQuotationManual, TYPE_MANUAL);
                }
                log.info("automatic: " + lsOrdersAutomatic.toString());
                log.info("manual: " + lsOrdersManual.toString());
                lsOrdersAutomatic.addAll(lsOrdersManual);

            } else {

                validateProductFinishTimer.manageStatusProductQuotation(notice.getId(), notice.getExternalEvent().toString(), null, false);
                validateProductFinishTimer.manageStatusQuotation(notice.getId());

            }
            Insurer insurer = insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(INSURER_NOT_FOUND, notice.getInsuranceNumber())));

            sendManualPurchase.setManualPurchasePieces(notice, insurer);


            if (notice.isAuth() && !lsOrdersAutomatic.isEmpty()) {
                List<Integer> positions = new ArrayList<>();
                for (Order order : lsOrdersAutomatic) {
                    order.getProducts().forEach(productOrder -> positions.add(productOrder.getPositionPiece()));
                }
                productQuotationRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positions, true);
                manualPurchaseRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positions, true);
            }

            log.info("finish manage notice: " + notice.getId() + " in: " + (System.currentTimeMillis() - startTime) + " ms");

            if (Boolean.TRUE.equals(serviceIntegrations))
                integrations.integrationByNotice(notice, insurer, lsOrdersAutomatic);

            noticeRepositoryPort.updateProcessedNoticeByNoticeId(noticeId);

            return true;
        } catch (ExceptionUtil ex) {
            ex.printStackTrace();
            throw new ExceptionUtil(ex.getStatusCode(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_MANAGE_NOTICE, noticeId), ex.getMessage());
        }

    }

}



