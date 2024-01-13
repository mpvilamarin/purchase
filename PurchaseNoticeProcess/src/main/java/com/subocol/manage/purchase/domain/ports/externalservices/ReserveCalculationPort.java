package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationDTO;

@Port
public interface ReserveCalculationPort {

    boolean sendReserveCalculationAdmin(ReserveCalculationDTO reserveCalculationDTO);

}
