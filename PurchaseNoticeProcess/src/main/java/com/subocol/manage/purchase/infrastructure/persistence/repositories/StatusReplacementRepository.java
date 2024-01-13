package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusReplacementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusReplacementRepository extends JpaRepository<StatusReplacementModel, Long>, JpaSpecificationExecutor<StatusReplacementModel> {
}