package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Desist;

import java.util.List;

@Port
public interface DesistRepositoryPort {

    Desist save(Desist desist);

    int saveAllNative(List<Desist> desistList);
}
