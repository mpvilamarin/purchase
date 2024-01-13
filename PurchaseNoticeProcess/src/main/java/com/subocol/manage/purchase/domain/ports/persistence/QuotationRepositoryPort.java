package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Quotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;

import java.util.List;
import java.util.Optional;

@Port
public interface QuotationRepositoryPort {

    List<CounterStatusProductQuotation> countStatusProductQuotationMM(Long noticeId);

    int updateStatusQuotation(Long quotationId, String status);

    Optional<Quotation> findById(Long quotationId);

    int updateQuotationManaged(List<Long> ids);

    CounterStatusProductQuotation countStatusProductQuotation(Long quotationId);

    List<Long> findQuotationWithAllProductManage(Long quotationId);

    Optional<Quotation> findQuotationByNoticeIdAndFlowType(Long noticeId);

    List<Long> findQuotationManagedByExternalEvent(String externalEvent);
}