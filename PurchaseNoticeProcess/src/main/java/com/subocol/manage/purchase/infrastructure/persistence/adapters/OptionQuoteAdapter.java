package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.domain.ports.persistence.OptionQuotePort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.OptionQuoteRepository;

import java.util.List;

@Adapter
public class OptionQuoteAdapter implements OptionQuotePort {

    private final OptionQuoteRepository repository;

    public OptionQuoteAdapter(OptionQuoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PiecesValuationDTO> findPiecesValuationMultibrandAccepted(Integer externalEvent) {
        return repository.getPiecesValuationMultibrandAccepted(externalEvent);
    }

    @Override
    public List<PiecesValuationDTO> findPiecesValuationMultibrandQuoted(Integer externalEvent) {
        return repository.getPiecesValuationMultibrandQuoted(externalEvent);
    }
}
