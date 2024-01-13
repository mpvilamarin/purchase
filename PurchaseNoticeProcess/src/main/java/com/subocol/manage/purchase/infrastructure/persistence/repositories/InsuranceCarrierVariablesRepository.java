package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierVariablesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsuranceCarrierVariablesRepository extends JpaRepository<InsuranceCarrierVariablesModel, Long>, JpaSpecificationExecutor<InsuranceCarrierVariablesModel> {

    Optional<InsuranceCarrierVariablesModel> findFirstByInsuranceId(Long insurerId);
}