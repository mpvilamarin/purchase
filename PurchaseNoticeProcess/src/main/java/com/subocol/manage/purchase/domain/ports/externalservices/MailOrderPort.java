package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;

import java.util.List;

@Port
public interface MailOrderPort {

    boolean sendOrderCreateNotification(List<MailOrderCreateDTO> mailData);

}
