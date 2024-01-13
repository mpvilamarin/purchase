package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderSubgroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderSubgroupRepository extends JpaRepository<ProviderSubgroupModel, Long>, JpaSpecificationExecutor<ProviderSubgroupModel> {
}