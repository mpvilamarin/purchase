package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.DataEvent;

import java.util.Optional;

@Port
public interface DataEventRepositoryPort {

    Optional<DataEvent> findById(Long id);

    Optional<DataEvent> findByExternalEventAndId(Integer externalEvent, Long id);

    int updateClaimNumberByExternalEvent(String event, String claimNumber);

    int updateAuthByExternalEvent(Integer event);

}
