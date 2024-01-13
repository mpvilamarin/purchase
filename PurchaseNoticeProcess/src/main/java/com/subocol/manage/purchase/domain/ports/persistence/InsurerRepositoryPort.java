package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Insurer;

import java.util.Optional;

@Port
public interface InsurerRepositoryPort {

    Optional<Insurer> findByInsurerId(Long insurerId);

}
