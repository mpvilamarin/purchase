package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.CurrencyModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusPartsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyModel, Long>, JpaSpecificationExecutor<CurrencyModel> {

    Optional<CurrencyModel> findByCountryId(Long countryId);
}