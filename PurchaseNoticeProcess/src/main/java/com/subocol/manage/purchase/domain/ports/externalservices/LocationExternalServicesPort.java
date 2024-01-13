package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Location;

@Port
public interface LocationExternalServicesPort {

    Location findLocation(Long id);

}
