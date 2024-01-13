package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;

@Port
public interface EnvironmentsRepositoryPort {

    String findEnvironmentsById(Long id);

}
