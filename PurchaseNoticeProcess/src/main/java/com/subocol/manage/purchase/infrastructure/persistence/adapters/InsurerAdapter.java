package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.ports.persistence.InsurerRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsurerRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 8/06/2023
 */
@Adapter
public class InsurerAdapter implements InsurerRepositoryPort {

    private final InsurerRepository repository;

    public InsurerAdapter(InsurerRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Insurer> findByInsurerId(Long insurerId) {
        return repository
                .findByInsurerId(insurerId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Insurer.class)));
    }

}
