package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.NoticeRepository;
import jakarta.persistence.Tuple;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */

@Adapter
public class NoticeAdapter implements NoticeRepositoryPort {

    private final NoticeRepository repository;

    public NoticeAdapter(NoticeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notice> findById(Long id) {
        return repository.findById(id).flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Notice.class)));
    }

    @Override
    public Notice save(Notice notice) {
        NoticeModel noticeSaved = repository.save(MapperUtil.convert(notice, NoticeModel.class));
        return MapperUtil.convert(noticeSaved, Notice.class);
    }

    @Override
    public Long countAllByExternalEventAndUnforeseen(Integer externalEvent, boolean unforeseen) {
        return repository.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen);
    }

    @Override
    public List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositions(Long externalEvent, List<Integer> positions, boolean deleted, boolean quoted) {
        return repository.findSpareToFollowUpByExternalEventAndPositions(externalEvent, positions, deleted, quoted);
    }

    @Override
    public List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositionsNoAuth(Long externalEvent) {
        List<SpareDetailToFollowUpDTO> result = new ArrayList<>();
        List<Tuple> resultQuery = repository.findSpareToFollowUpByExternalEventAndPositionsNoAuth(externalEvent);
        for (Tuple tuple : resultQuery) {
            result.add(new SpareDetailToFollowUpDTO(tuple.get("position_piece", Integer.class),
                    tuple.get("quantity", BigDecimal.class).intValue(),
                    tuple.get("esCotizado", Boolean.class),
                    tuple.get("deleted", Boolean.class)));
        }
        return result;
    }

    @Override
    public NoticeClaimNumberDTO findDistinctClaimNumberByExternalEvent(String externalEvent, String claimNumber) {
        return repository.findDistinctClaimNumberByExternalEvent(Integer.valueOf(externalEvent), claimNumber);
    }

    @Override
    public int updateClaimNumberByExternalEvent(String externalEvent, String claimNumber) {
        return repository.updateClaimNumberByExternalEvent(Integer.valueOf(externalEvent), claimNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> findAllByExternalEvent(String externalEvent) {

        List<NoticeModel> result = repository.findAllByExternalEvent(Integer.valueOf(externalEvent));

        if (!result.isEmpty())
            return MapperUtil.convertList(result, Notice.class);

        return Collections.emptyList();
    }

    @Override
    public int updateAuthByExternalEvent(String externalEvent, Boolean auth) {
        return repository.updateAuthByExternalEvent(Integer.valueOf(externalEvent), auth);
    }

    @Override
    public List<Notice> findAllByExternalEventAndAuth(String externalEvent, boolean auth) {
        List<NoticeModel> result = repository.findByExternalEventAndAuth(Integer.parseInt(externalEvent), auth);
        if (!result.isEmpty())
            return MapperUtil.convertList(result, Notice.class);

        return Collections.emptyList();
    }

    @Override
    public List<SpareDetailToFollowUpDTO> findInfoFollowUpByExternalEventAndPositions(Long externalEvent, List<Integer> positions, boolean deleted, boolean quoted) {
        return repository.findInfoFollowUpByExternalEventAndPositions(externalEvent, positions, deleted, quoted);
    }

    @Override
    public List<SpareDetailToFollowUpDTO> findInfoFollowUpByExternalEventAndPositionsNoAut(Long externalEvent) {
        List<Object[]> rows = repository.findInfoFollowUpByExternalEventAndPositionsNoAut(externalEvent);
        List<SpareDetailToFollowUpDTO> response = new ArrayList<>();
        for (Object[] row : rows) {
            SpareDetailToFollowUpDTO repuestoInfoSeguimientoDTO = new SpareDetailToFollowUpDTO((Integer) row[0],
                    ((BigDecimal) row[1]).intValue(), (Boolean) row[2], (Boolean) row[3]);
            response.add(repuestoInfoSeguimientoDTO);
        }
        return response;
    }

    @Override
    public int updateProcessedNoticeByNoticeId(Long noticeId) {
        return repository.updateProcessedNoticeByNoticeId(noticeId);
    }

}
