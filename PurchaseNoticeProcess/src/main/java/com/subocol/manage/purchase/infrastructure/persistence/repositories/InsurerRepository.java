package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.InsurerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsurerRepository extends JpaRepository<InsurerModel, Long>, JpaSpecificationExecutor<InsurerModel> {

    Optional<InsurerModel> findByInsurerId(Long insurerId);

}