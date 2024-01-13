package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.SubsidiaryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubsidiaryRepository extends JpaRepository<SubsidiaryModel, Long>, JpaSpecificationExecutor<SubsidiaryModel> {


    Optional<SubsidiaryModel> findFirstByOrders_Id(Long orderId);


}