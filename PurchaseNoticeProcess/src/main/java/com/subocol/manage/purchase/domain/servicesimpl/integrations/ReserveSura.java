package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveSuraPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveSura {

    private final NoticeRepositoryPort noticeRepositoryPort;
    private final PieceRepositoryPort pieceRepositoryPort;
    private final SendReserveManageRepositoryPort sendReserveManageRepository;
    private final ProductOrdersPiecesNoticeRepositoryPort productOrdersPiecesNoticeRepositoryPort;
    private final ProductOrderRepositoryPort productOrderRepositoryPort;
    private final ReserveSuraPort reserveSuraPort;


    public boolean sendPiecesReserveSura(Insurer insurer, Notice notice) {

        ReserveCalculationSuraDTO reserveCalculationDTO = null;
        ReserveCalculationSuraDTO reserveCalculationInitDTO = null;
        boolean response = false;
        Long countProductOrdersInit = -1L;

        try {
            if (insurer != null && insurer.getFlowReserveSura() && notice.isAuth()) {
                log.info("INICIA PROCESO ENVIO PIEZAS RESERVA SURA");

                List<Integer> initPositionPieces = validateInitPositionsSura(notice.getExternalEvent(), false);
                List<Integer> unforeseenPositionPieces = validateInitPositionsSura(notice.getExternalEvent(), true);

                Optional<SendReserveManage> sendReserveManage = sendReserveManageRepository.findByExternalEvent(notice.getExternalEvent());
                if (sendReserveManage.isPresent() && Boolean.TRUE.equals(sendReserveManage.get().getInitCarSended())) {
                    reserveCalculationDTO = calculateValuesReserveSura(notice, initPositionPieces, unforeseenPositionPieces);
                    if (reserveCalculationDTO != null) {
                        reserveSuraPort.sendPiecesReserveAdminSura(reserveCalculationDTO);
                        response = true;
                    }
                } else {

                    List<Integer> initPositionPiecesWithoutDesist = validateInitPositionsWithoutDesistSura(notice.getExternalEvent(), false);

                    if (!initPositionPiecesWithoutDesist.isEmpty()) {
                        countProductOrdersInit = productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(
                                notice.getExternalEvent(), initPositionPiecesWithoutDesist);
                    }

                    if ((countProductOrdersInit == initPositionPiecesWithoutDesist.size())
                            || (initPositionPieces.isEmpty())) {
                        reserveCalculationInitDTO = calculateValuesReserveSura(notice, initPositionPieces, unforeseenPositionPieces);
                        if (reserveCalculationInitDTO != null) {
                            reserveSuraPort.sendPiecesReserveAdminSura(reserveCalculationInitDTO);
                            createSendReserveManage(notice.getExternalEvent());
                            response = true;
                        }
                    }
                }
            }

            log.info("TERMINA ENVIO DE PIEZAS A RESERVA SURA");
            return response;
        } catch (Exception e) {
            log.info("sendPiecesReserveSura: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> validateInitPositionsSura(Integer externalEvent, boolean unforeseen) {
        List<Integer> initPositionPieces = null;
        try {
            Long countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen);
            if (countInitPackage == 1) {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionTrue(externalEvent, unforeseen);
            } else {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionFalse(externalEvent, unforeseen);
            }
        } catch (Exception e) {
            log.info("validateInitPositionsSura: " + e.toString());
            e.printStackTrace();
            return null;

        }
        return initPositionPieces;
    }


    public ReserveCalculationSuraDTO calculateValuesReserveSura(Notice notice, List<Integer> positionInit, List<Integer> positionsUnforeseen) {

        ReserveCalculationSuraDTO response = new ReserveCalculationSuraDTO();
        ReserveCalculationTotalSuraDTO reserveCalculationInit = new ReserveCalculationTotalSuraDTO();
        ReserveCalculationTotalSuraDTO reserveCalculationUnforeseen = new ReserveCalculationTotalSuraDTO();
        List<Integer> positionsFinal = new ArrayList<>(0);
        try {

            if (!positionInit.isEmpty()) {
                reserveCalculationInit =
                        productOrdersPiecesNoticeRepositoryPort.totalGrossPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), positionInit, false);
                positionsFinal.addAll(positionInit);
            }

            if (!positionsUnforeseen.isEmpty()) {
                reserveCalculationUnforeseen =
                        productOrdersPiecesNoticeRepositoryPort.totalGrossPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), positionsUnforeseen, true);
                positionsFinal.addAll(positionsUnforeseen);
            }

            List<ReserveRepuestosSuraDTO> listSpares = productOrdersPiecesNoticeRepositoryPort.findPiecesOrdersByExternalEvent(notice.getExternalEvent(), ManageNoticeConstant.LIST_SPARE, positionsFinal);

            response.setNumeroAviso(notice.getExternalEvent());
            response.setPedidoInicial(reserveCalculationInit);
            response.setImprevistos(reserveCalculationUnforeseen);
            response.setRepuesto(listSpares);

        } catch (Exception e) {
            log.info("calculateValuesReserveSura: " + e.toString());
            e.printStackTrace();
            return null;

        }
        return response;
    }

    public List<Integer> validateInitPositionsWithoutDesistSura(Integer externalEvent, boolean unforeseen) {
        List<Integer> initPositionPieces = null;
        List<Integer> initPositionPiecesDesist = null;
        try {
            Long countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen);
            if (countInitPackage == 1) {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionTrue(externalEvent, unforeseen);
                initPositionPiecesDesist = pieceRepositoryPort.findInitialPiecesByExternalWithOrdersSuraConditionTrue(externalEvent, unforeseen);
            } else {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionFalse(externalEvent, unforeseen);
                initPositionPiecesDesist = pieceRepositoryPort.findInitialPiecesByExternalWithOrdersSuraConditionFalse(externalEvent, unforeseen);
            }

            if (!initPositionPiecesDesist.isEmpty()) {
                initPositionPieces.removeAll(initPositionPiecesDesist);
            }
        } catch (Exception e) {
            log.info("validateInitPositionsWithoutDesistSura: " + e.toString());
            e.printStackTrace();

        }
        return initPositionPieces;
    }

    public SendReserveManage createSendReserveManage(Integer externalEvent) throws ExceptionUtil {
        SendReserveManage sendReserve = new SendReserveManage();
        try {
            sendReserve.setExternalEvent(externalEvent);
            sendReserve.setInitCarSended(true);
            sendReserve.setDate(TimeZoneUtil.getTimestampByDefaultZone());
            sendReserveManageRepository.save(sendReserve);
        } catch (Exception e) {
            log.info("createSendReserveManage: " + e.toString());
            e.printStackTrace();
            return null;

        }
        return sendReserve;
    }

    public boolean sendPurchaseTotalReserveDesistSura(Notice notice) {
        boolean response = false;
        try {
            log.info("Inicia proceso de reserva sura desist");
            List<Integer> initPositionPieces = validateInitPositionsSura(notice.getExternalEvent(), false);
            List<Integer> unforeseenPositionPieces = validateInitPositionsSura(notice.getExternalEvent(), true);

            Optional<SendReserveManage> sendReserveManage = sendReserveManageRepository.findByExternalEvent(notice.getExternalEvent());
            if (sendReserveManage.isPresent() && Boolean.TRUE.equals(sendReserveManage.get().getInitCarSended())) {
                ReserveCalculationSuraDTO reserveCalculationDTO = calculateValuesReserveSura(notice, initPositionPieces, unforeseenPositionPieces);
                if (reserveCalculationDTO != null) {
                    reserveSuraPort.sendPiecesReserveAdminSura(reserveCalculationDTO);
                    response = true;
                }
            }

        } catch (Exception e) {
            log.info("sendPurchaseTotalReserveDesistSura: " + e);
        }
        return response;
    }
}
