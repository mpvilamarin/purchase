package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationBolivarDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalBolivarDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.ReserveBolivarAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.INSURER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveBolivar {

    private final InsurerRepositoryPort insurerRepositoryPort;
    private final NoticeRepositoryPort noticeRepositoryPort;
    private final PieceRepositoryPort pieceRepositoryPort;

    private final ProductOrdersPiecesNoticeRepositoryPort productOrdersPiecesNoticeRepositoryPort;
    private final ReserveBolivarAdapter reserveBolivarAdapter;
    private final SendReserveManageRepositoryPort sendReserveManageRepositoryPort;
    private final ProductOrderRepositoryPort productOrderRepositoryPort;

    public boolean sendPurchaseTotalReserve(Notice notice) {

        ReserveCalculationBolivarDTO reserveCalculationDTO = null;
        ReserveCalculationBolivarDTO reserveCalculationInitDTO = null;
        boolean response = false;
        Long countProductOrdersInit = -1L;
        try {

            Insurer insurer = insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(INSURER_NOT_FOUND, notice.getInsuranceNumber())));
            if (insurer != null && insurer.getFlowReserveBolivar()) {

                List<Integer> initPositionPieces = validateInitPositions(notice.getExternalEvent(), false);
                List<Integer> unforeseenPositionPieces = validateInitPositions(notice.getExternalEvent(), true);
                Optional<SendReserveManage> sendReserveManage = sendReserveManageRepositoryPort.findByExternalEvent(notice.getExternalEvent());
                if (sendReserveManage.isPresent() && Boolean.TRUE.equals(sendReserveManage.get().getInitCarSended())) {
                    reserveCalculationDTO = calculateValuesReserve(notice, initPositionPieces, unforeseenPositionPieces);
                    if (reserveCalculationDTO != null) {
                        reserveBolivarAdapter.sendReserveCalculationAdmin(reserveCalculationDTO);
                        response = true;
                    }
                } else {

                    List<Integer> initPositionPiecesWithoutDesist = validateInitPositionsWithoutDesist(notice.getExternalEvent(), false);
                    if (!initPositionPiecesWithoutDesist.isEmpty()) {
                        countProductOrdersInit = productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(
                                notice.getExternalEvent(), initPositionPiecesWithoutDesist);
                    }

                    if ((countProductOrdersInit == initPositionPiecesWithoutDesist.size())
                            || (initPositionPieces.isEmpty())) {
                        reserveCalculationInitDTO = calculateValuesReserve(notice, initPositionPieces, unforeseenPositionPieces);
                        if (reserveCalculationInitDTO != null) {
                            reserveBolivarAdapter.sendReserveCalculationAdmin(reserveCalculationInitDTO);
                            createSendReserveManage(notice.getExternalEvent());
                            response = true;
                        }
                    }
                }

                return response;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public List<Integer> validateInitPositions(Integer externalEvent, boolean unforeseen) {
        List<Integer> initPositionPieces = null;
        try {
            Long countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen);
            if (countInitPackage == 1) {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);
            } else {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(externalEvent, unforeseen);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return List.of();
        }
        return initPositionPieces;
    }

    public ReserveCalculationBolivarDTO calculateValuesReserve(Notice notice, List<Integer> positionInit, List<Integer> positionsUnforeseen) {

        ReserveCalculationBolivarDTO response = new ReserveCalculationBolivarDTO();
        List<ReserveCalculationTotalBolivarDTO> listInit = new ArrayList<>(0);
        List<ReserveCalculationTotalBolivarDTO> listUnforeseen = new ArrayList<>(0);

        try {

            if (!positionInit.isEmpty()) {
                ReserveCalculationTotalBolivarDTO reserveCalculationInit = new ReserveCalculationTotalBolivarDTO();
                reserveCalculationInit.setTotalRepuNeto(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), true, positionInit, false));
                reserveCalculationInit.setTotalTotNeto(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), false, positionInit, false));
                listInit.add(reserveCalculationInit);
            } else {
                listInit.add(new ReserveCalculationTotalBolivarDTO());
            }

            if (!positionsUnforeseen.isEmpty()) {
                ReserveCalculationTotalBolivarDTO reserveCalculationUnforeseen = new ReserveCalculationTotalBolivarDTO();
                reserveCalculationUnforeseen.setTotalRepuNeto(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), true, positionsUnforeseen, true));
                reserveCalculationUnforeseen.setTotalTotNeto(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), false, positionsUnforeseen, true));
                listUnforeseen.add(reserveCalculationUnforeseen);
            } else {
                listUnforeseen.add(new ReserveCalculationTotalBolivarDTO());
            }

            response.setNumeroAviso(notice.getExternalEvent());
            response.setPedidoInicial(listInit);
            response.setImprevistos(listUnforeseen);

        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public List<Integer> validateInitPositionsWithoutDesist(Integer externalEvent, boolean unforeseen) {
        List<Integer> initPositionPieces = null;
        List<Integer> initPositionPiecesDesist = null;
        try {
            Long countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen);
            if (countInitPackage == 1) {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);
                initPositionPiecesDesist = pieceRepositoryPort.findInitialPiecesByExternalWithOrdersConditionTrue(externalEvent, unforeseen);
            } else {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(externalEvent, unforeseen);
                initPositionPiecesDesist = pieceRepositoryPort.findInitialPiecesByExternalWithOrdersConditionFalse(externalEvent, unforeseen);
            }

            if (!initPositionPiecesDesist.isEmpty()) {
                initPositionPieces.removeAll(initPositionPiecesDesist);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return List.of();
        }
        return initPositionPieces;
    }

    public SendReserveManage createSendReserveManage(Integer externalEvent) {
        SendReserveManage sendReserve = new SendReserveManage();
        try {
            sendReserve.setExternalEvent(externalEvent);
            sendReserve.setInitCarSended(true);
            sendReserve.setDate(TimeZoneUtil.getTimestampByDefaultZone());
            sendReserveManageRepositoryPort.save(sendReserve);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return sendReserve;
    }


    public boolean sendPurchaseTotalReserveDesistBolivar(Notice notice) {
        boolean response = false;
        try {
            log.info("Incia proceso reserva Bolivar desist");
            List<Integer> initPositionPieces = validateInitPositions(notice.getExternalEvent(), false);
            List<Integer> unforeseenPositionPieces = validateInitPositions(notice.getExternalEvent(), true);

            Optional<SendReserveManage> sendReserveManage = sendReserveManageRepositoryPort.findByExternalEvent(notice.getExternalEvent());
            if (sendReserveManage.isPresent() && Boolean.TRUE.equals(sendReserveManage.get().getInitCarSended())) {
                ReserveCalculationBolivarDTO reserveCalculationDTO = calculateValuesReserve(notice, initPositionPieces, unforeseenPositionPieces);
                if (reserveCalculationDTO != null) {
                    reserveBolivarAdapter.sendReserveCalculationAdmin(reserveCalculationDTO);
                    response = true;
                }
            }
        } catch (Exception e) {
            log.info("sendPurchaseTotalReserveDesist: " + e);
        }
        return response;
    }

}
