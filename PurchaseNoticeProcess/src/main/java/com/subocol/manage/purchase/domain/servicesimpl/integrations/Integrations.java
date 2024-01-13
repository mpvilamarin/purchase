package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.ThrowingConsumer;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.ports.persistence.InsurerRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.OrderRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_INTERNAL_EXTERNAL_SERVICES;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.NOTICE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class Integrations {

    private final NoticeRepositoryPort noticeRepositoryPort;
    private final InsurerRepositoryPort insurerRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final MailOrderImpl mailOrderIntegration;
    private final AlertPiecesValuationImpl alertPiecesValuation;
    private final ReserveBolivar reserveBolivar;
    private final ReserveSura reserveSura;
    private final BillingOrders billingOrders;
    private final ChileOrderImpl chileOrders;
    private final FollowUp followUp;

    public boolean integrationByNotice(Notice notice, Insurer insurer, Set<Order> orders) throws ExceptionUtil {

        try {
            List<MailOrderCreateDTO> mailOrderCreated = new CopyOnWriteArrayList<>();

            if (Boolean.TRUE.equals(insurer.getUseOrbikaValuation())) {
                alertPiecesValuation.alertPiecesValuation(notice.getExternalEvent(), notice.isAuth(), notice.getWorkshopType());
            }

            if (orders != null && !orders.isEmpty()) {
                if (Boolean.TRUE.equals(insurer.getFlowReserveBolivar()) && insurer.getName().equalsIgnoreCase(ManageNoticeConstant.BOLIVAR)) {
                    reserveBolivar.sendPurchaseTotalReserve(notice);
                } else if (Boolean.TRUE.equals(insurer.getFlowReserveSura()) && insurer.getName().equalsIgnoreCase(ManageNoticeConstant.SURA)) {
                    reserveSura.sendPiecesReserveSura(insurer, notice);
                }
                orders.forEach(ThrowingConsumer.wrapExceptions(order -> {

                            if (Boolean.TRUE.equals(insurer.getSendOrderFact())) {
                                billingOrders.sendBillingOrder(order, notice);
                            }
                            if (Boolean.TRUE.equals(insurer.getSdkActive())) {
                                chileOrders.chileBuyingOrder(order);
                            }

                            if (Boolean.TRUE.equals(insurer.getSendOrderEmailWinner())) {
                                mailOrderIntegration.addMailOrderNotification(
                                        notice.getExternalEvent().toString(),
                                        order.getId(),
                                        order.getPurchaseTypeId(),
                                        mailOrderCreated, notice.getPlate(), insurer.getName());
                            }
                        })

                );
                followUp.sendPurchasesToFollowUp(notice.getExternalEvent().longValue(), orders);
            }

            if (!mailOrderCreated.isEmpty())
                mailOrderIntegration.sendMailOrderNotification(mailOrderCreated);

            log.info("Se realizaron las integraciones correctamente");

            return true;

        } catch (ExceptionUtil e) {
            e.printStackTrace();
            throw new ExceptionUtil(e.getStatusCode(), e.getMessage(), e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO integrationServicesExposed(Long noticeId, Long insurerId, List<Long> orderIds) throws ExceptionUtil {

        try {
            ResponseDTO response = new ResponseDTO();

            Notice notice = noticeRepositoryPort.findById(noticeId)
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(NOTICE_NOT_FOUND, noticeId)));

            Insurer insurer = insurerRepositoryPort.findByInsurerId(insurerId)
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.INSURER_NOT_FOUND));

            List<Order> orders = orderRepositoryPort.findAllById(orderIds);

            Set<Order> orderSet = new HashSet<>(orders);

            integrationByNotice(notice, insurer, orderSet);

            response.setMessage("Se proceso correctamente el aviso:  " + notice.getExternalEvent() + " NoticeId " + notice.getId());
            response.setStatus(200);
            response.setSuccess(true);
            log.info(response.getMessage());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = ErrorMessageHandler.concatenateStringAndObject(ERROR_INTERNAL_EXTERNAL_SERVICES, noticeId);
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, e.getMessage());

        }
    }

}
