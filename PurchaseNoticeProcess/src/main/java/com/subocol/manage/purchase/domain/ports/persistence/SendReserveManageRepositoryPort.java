package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.SendReserveManage;

import java.util.Optional;

@Port
public interface SendReserveManageRepositoryPort {

    Optional<SendReserveManage> findByExternalEvent(Integer externalEvent);

    SendReserveManage save(SendReserveManage sendReserveManage);
}
