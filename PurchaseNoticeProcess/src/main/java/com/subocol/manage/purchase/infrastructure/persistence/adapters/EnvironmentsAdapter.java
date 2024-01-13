package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.domain.ports.persistence.EnvironmentsRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.EnvironmentsModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.EnvironmentsRepository;

/**
 * @author DANR
 * @version 1.0
 * @since 29/06/2023
 */
@Adapter
public class EnvironmentsAdapter implements EnvironmentsRepositoryPort {

    private final EnvironmentsRepository repository;

    public EnvironmentsAdapter(EnvironmentsRepository repository) {
        this.repository = repository;
    }

    @Override
    public String findEnvironmentsById(Long id) {

        return repository
                .findById(id)
                .map(EnvironmentsModel::getEnvironments)
                .orElse(null);

    }


}
