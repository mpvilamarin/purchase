package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Subsidiary;

import java.util.Optional;

@Port
public interface SubsidiaryRepositoryPort {

    Optional<Subsidiary> findById(Long subsidiaryId);

    Optional<Subsidiary> findByOrderId(Long orderId);

}
