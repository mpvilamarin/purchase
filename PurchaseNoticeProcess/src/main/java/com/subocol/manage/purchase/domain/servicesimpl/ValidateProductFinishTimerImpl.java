package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.ThrowingConsumer;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.ProductQuotation;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.QuotationRepositoryPort;
import com.subocol.manage.purchase.domain.services.ValidateProductFinishTimer;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_CHANGE_STATUS;


@Service
@RequiredArgsConstructor
public class ValidateProductFinishTimerImpl implements ValidateProductFinishTimer {


    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;
    private final QuotationRepositoryPort quotationRepositoryPort;

    @Override
    public int updateActiveProductQuotation(Long noticeId) {

        return productQuotationRepositoryPort.updateActiveProductQuotation(noticeId, false);

    }

    @Override
    public List<ProductQuotation> findProductsQuotationByPriorityOne(Long noticeId, String externalEvent) {
        return productQuotationRepositoryPort.findByEventAndPriorityQuotation(noticeId, externalEvent);
    }

    @Override
    public void manageStatusProductQuotation(Long noticeId, String externalEvent, List<Long> produtsQuotationIds, boolean noticeAuth) throws ExceptionUtil {
        try {
            productQuotationRepositoryPort.updateStatusManageQuotationPieces(noticeId, externalEvent,
                    ManageNoticeConstant.AUTOMATIC, ManageNoticeConstant.SENT,
                    ManageNoticeConstant.OMITTED);

            if (Boolean.TRUE.equals(noticeAuth) && produtsQuotationIds != null && !produtsQuotationIds.isEmpty())
                productQuotationRepositoryPort.updateStatusManageQuotationPiecesOptionQuote(produtsQuotationIds, ManageNoticeConstant.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_CHANGE_STATUS, "ProductQuotation"), e.getMessage());
        }

    }

    @Override
    public void manageStatusQuotation(Long noticeId) throws ExceptionUtil {
        try {
            List<CounterStatusProductQuotation> listQuotationCounters = quotationRepositoryPort.countStatusProductQuotationMM(noticeId);

            listQuotationCounters.forEach(
                    ThrowingConsumer.wrapExceptions(
                            quotationCounters -> ManageNoticeCC.changeStatusQuotation(quotationCounters, quotationRepositoryPort)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_CHANGE_STATUS, "quotation"), e.getMessage());
        }


    }

}



