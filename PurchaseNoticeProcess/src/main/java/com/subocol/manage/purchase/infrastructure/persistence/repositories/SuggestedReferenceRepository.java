package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.SuggestedReferenceModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestedReferenceRepository extends JpaRepository<SuggestedReferenceModel, Integer>, JpaSpecificationExecutor<SuggestedReferenceModel> {


    @Query("SELECT sr.reference " +
            "FROM SuggestedReferenceModel sr " +
            "INNER JOIN PieceModel p ON p.id = sr.piece.id " +
            "WHERE p.position = ?2 " +
            "AND p.dataEvent.id = ?1 " +
            "ORDER BY sr.id DESC")
    List<String> getAllReferenceByPiece_PositionAndDataEvent_ID(Long eventId, Integer position, Pageable pageable);

}