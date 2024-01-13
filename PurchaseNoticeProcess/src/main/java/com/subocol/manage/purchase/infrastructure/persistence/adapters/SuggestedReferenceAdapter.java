package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.domain.ports.persistence.SuggestedReferenceRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SuggestedReferenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 8/06/2023
 */
@Adapter
public class SuggestedReferenceAdapter implements SuggestedReferenceRepositoryPort {

    private final SuggestedReferenceRepository repository;

    public SuggestedReferenceAdapter(SuggestedReferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> findListSuggestedReferenceByEventAndPosition(Long eventId, Integer position, Integer cantRefToShow) {

        Pageable pageable = PageRequest.of(0, cantRefToShow);
        return repository.getAllReferenceByPiece_PositionAndDataEvent_ID(eventId, position, pageable);

    }

}
