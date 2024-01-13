package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.StatusReplacement;

@Port
public interface StatusReplacementRepositoryPort {

    StatusReplacement save(StatusReplacement statusReplacement);

}
