package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;

import java.util.List;

@Port
public interface OptionQuotePort {

    List<PiecesValuationDTO> findPiecesValuationMultibrandAccepted(Integer externalEvent);

    List<PiecesValuationDTO> findPiecesValuationMultibrandQuoted(Integer externalEvent);

}
