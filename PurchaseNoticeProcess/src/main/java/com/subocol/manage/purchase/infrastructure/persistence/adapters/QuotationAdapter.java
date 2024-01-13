package com.subocol.manage.purchase.infrastructure.persistence.adapters;


import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Quotation;
import com.subocol.manage.purchase.domain.ports.persistence.QuotationRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.QuotationRepository;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Slf4j
@Adapter
public class QuotationAdapter implements QuotationRepositoryPort {

    private final QuotationRepository repository;

    public QuotationAdapter(QuotationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CounterStatusProductQuotation> countStatusProductQuotationMM(Long noticeId) {
        return repository.getCounterStatusProductQuotation(noticeId);
    }

    @Override
    public int updateStatusQuotation(Long quotationId, String status) {
        return repository.updateQuotationStatusById(status, quotationId);
    }

    @Override
    public Optional<Quotation> findById(Long quotationId) {
        return repository.findById(quotationId).flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Quotation.class)));
    }

    @Override
    public int updateQuotationManaged(List<Long> ids) {
        return repository.updateQuotationManaged(ids);
    }

    @Override
    public CounterStatusProductQuotation countStatusProductQuotation(Long quotationId) {
        return repository.countStatusProductQuotation(quotationId);
    }

    @Override
    public List<Long> findQuotationWithAllProductManage(Long quotationId) {
        List<Long> ids = new ArrayList<>();
        List<Tuple> result = repository.findQuotationWithAllProductManage(quotationId);

        if (!result.isEmpty()) {
            ids = result.stream().map(tuple -> tuple.get("id", Long.class)).toList();
        }
        return ids;
    }

    @Override
    public Optional<Quotation> findQuotationByNoticeIdAndFlowType(Long noticeId) {

        return repository.findQuotationByNoticeIdAndFlowType(noticeId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Quotation.class)));
    }

    @Override
    public List<Long> findQuotationManagedByExternalEvent(String externalEvent) {
        List<Long> ids = new ArrayList<>();
        List<Tuple> result = repository.findQuotationManagedByExternalEvent(externalEvent);

        if (!result.isEmpty()) {
            ids = result.stream().map(tuple -> tuple.get("id", Long.class)).toList();
        }
        return ids;
    }
}
