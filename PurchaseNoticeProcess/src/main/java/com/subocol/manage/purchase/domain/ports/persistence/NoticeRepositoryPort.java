package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;

import java.util.List;
import java.util.Optional;

@Port
public interface NoticeRepositoryPort {

    Optional<Notice> findById(Long noticeId);

    Notice save(Notice notice);

    Long countAllByExternalEventAndUnforeseen(Integer externalEvent, boolean unforeseen);

    List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositions(Long externalEvent, List<Integer> positions, boolean deleted, boolean quoted);

    List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositionsNoAuth(Long externalEvent);

    NoticeClaimNumberDTO findDistinctClaimNumberByExternalEvent(String externalEvent, String claimNumber);

    int updateClaimNumberByExternalEvent(String externalEvent, String claimNumber);

    List<Notice> findAllByExternalEvent(String externalEvent);

    int updateAuthByExternalEvent(String externalEvent, Boolean auth);

    List<Notice> findAllByExternalEventAndAuth(String externalEvent, boolean auth);

    List<SpareDetailToFollowUpDTO> findInfoFollowUpByExternalEventAndPositions(Long externalEvent, List<Integer> positions, boolean deleted, boolean quoted);

    List<SpareDetailToFollowUpDTO> findInfoFollowUpByExternalEventAndPositionsNoAut(Long externalEvent);

    int updateProcessedNoticeByNoticeId(Long noticeId);
}