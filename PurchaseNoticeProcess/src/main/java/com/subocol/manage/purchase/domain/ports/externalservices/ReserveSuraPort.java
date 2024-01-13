package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationSuraDTO;

@Port
public interface ReserveSuraPort {

    String sendPiecesReserveAdminSura(ReserveCalculationSuraDTO reserveCalculationDTO);
}
