package com.subocol.manage.purchase.domain.services;

import com.subocol.manage.purchase.common.annotations.ServiceDeclaration;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.models.ProductQuotation;

import java.util.List;

@ServiceDeclaration
public interface ValidateProductFinishTimer {
    int updateActiveProductQuotation(Long noticeId);

    List<ProductQuotation> findProductsQuotationByPriorityOne(Long noticeId, String externalEvent);

    void manageStatusProductQuotation(Long noticeId, String externalEvent, List<Long> produtsQuotationIds, boolean noticeAuth) throws ExceptionUtil;

    void manageStatusQuotation(Long noticeId) throws ExceptionUtil;

}