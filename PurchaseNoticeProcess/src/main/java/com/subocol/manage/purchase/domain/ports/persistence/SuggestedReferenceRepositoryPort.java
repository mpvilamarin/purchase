package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;

import java.util.List;


@Port
public interface SuggestedReferenceRepositoryPort {

    List<String> findListSuggestedReferenceByEventAndPosition(Long eventId, Integer position, Integer cantRefToShow);

}
