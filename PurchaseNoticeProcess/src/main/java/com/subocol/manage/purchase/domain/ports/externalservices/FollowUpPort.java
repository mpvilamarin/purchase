package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;

@Port
public interface FollowUpPort {

    boolean sendDataToSFollowUp(SendSparesToFollowUPDTO sendSparesToFollowUPDTO);

}
