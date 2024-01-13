package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationBolivarDTO;

@Port
public interface ReserveBolivarPort {

    String sendReserveCalculationAdmin(ReserveCalculationBolivarDTO reserveCalculationBolivarDTO);

}
