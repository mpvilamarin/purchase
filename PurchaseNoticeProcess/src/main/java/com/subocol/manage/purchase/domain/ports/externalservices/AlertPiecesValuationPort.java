package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;

@Port
public interface AlertPiecesValuationPort {

    String serviceAlertPiecesValuation(NoticeValuationDTO noticeValuationDTO);
}
