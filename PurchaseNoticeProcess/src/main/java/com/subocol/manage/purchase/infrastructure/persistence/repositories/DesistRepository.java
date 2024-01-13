package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DesistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DesistRepository extends JpaRepository<DesistModel, Long>, JpaSpecificationExecutor<DesistModel> {
}
