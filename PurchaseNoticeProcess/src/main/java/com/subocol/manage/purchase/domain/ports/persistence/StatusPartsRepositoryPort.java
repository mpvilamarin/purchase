package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.StatusParts;

import java.util.List;

@Port
public interface StatusPartsRepositoryPort {

    int saveAllNative(List<StatusParts> statusPartsList);

    int updateStatusByProductOrderId(String status, List<Long> productOrderIds);
}
