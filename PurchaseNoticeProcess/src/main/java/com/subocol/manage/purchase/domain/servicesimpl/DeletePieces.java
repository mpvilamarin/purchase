package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.application.dtos.DeletePiecesManualPurchaseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.Common;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.models.DeletedPiecesHistory;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.externalservices.FollowUpPort;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveCalculationPort;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveSuraPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;

/**
 * @author DANR
 * @version 1.0
 * @since 23/08/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeletePieces {

    private final DeletedPiecesHistoryPort deletedPiecesHistoryPort;

    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;

    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;

    private final NoticeRepositoryPort noticeRepositoryPort;

    private final InsurerRepositoryPort insurerRepositoryPort;

    private final PieceRepositoryPort pieceRepositoryPort;

    private final ProductOrderRepositoryPort productOrderRepositoryPort;

    private final SendReserveManageRepositoryPort sendReserveManageRepositoryPort;

    private final FollowUpPort followUpPort;

    private final ReserveSuraPort reserveSuraPort;

    private final ReserveCalculationPort reserveCalculationPort;

    @Transactional(rollbackFor = Exception.class)
    public boolean deletePieces(DeletePiecesManualPurchaseDTO deletePiecesDTO) {

        try {

            DeletedPiecesHistory deletedPiecesHistory = DeletedPiecesHistory.builder()
                    .externalEvent(deletePiecesDTO.getExternalEvent())
                    .positionPiece(deletePiecesDTO.getPositionPiece())
                    .userName(deletePiecesDTO.getUserName())
                    .deletedCause(deletePiecesDTO.getDeletedCause())
                    .deletedDate(TimeZoneUtil.getTimestampByDefaultZone())
                    .build();

            deletedPiecesHistoryPort.save(deletedPiecesHistory);
            productQuotationRepositoryPort.updateDeletePiecesTrueByExternalEventAndPosition(deletePiecesDTO.getExternalEvent(), deletedPiecesHistory.getPositionPiece());
            manualPurchaseRepositoryPort.updateDeletePiecesTrueByExternalEventAndPosition(deletePiecesDTO.getExternalEvent(), deletedPiecesHistory.getPositionPiece());

            validateReserve(deletePiecesDTO.getExternalEvent());
            validateReserveSura(deletePiecesDTO.getExternalEvent());
            sendSparePartInformationFollowUpDeleted(Long.valueOf(deletePiecesDTO.getExternalEvent()), deletePiecesDTO.getPositionPiece());
            return true;
        } catch (ExceptionUtil e) {
            log.error(e.getMessage() + e.getCause());
            throw new ExceptionUtil(e.getStatusCode(), e.getMessage(), e.getMessage());
        }
    }

    public boolean validateReserve(String externalEvent) {

        boolean response = false;
        Long countProductOrdersInit = -1L;

        try {

            Notice notice = noticeRepositoryPort.findAllByExternalEvent(externalEvent).get(0);
            Insurer insurer = insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.INSURER_NOT_FOUND));

            if (insurer != null && insurer.getFlowReserveBolivar() && notice.isAuth()) {
                ReserveCalculationDTO reserveCalculationInitDTO;
                List<Integer> initPositionPieces = validateInitPositions(notice.getExternalEvent(), false);
                List<Integer> initPositionPiecesWithoutDesist = validateInitPositionsWithoutDesist(notice.getExternalEvent(), false);
                List<Integer> unforeseenPositionPieces = validateInitPositions(notice.getExternalEvent(), true);

                if (!initPositionPiecesWithoutDesist.isEmpty()) {
                    countProductOrdersInit = productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(
                            notice.getExternalEvent(), initPositionPiecesWithoutDesist);
                }

                SendReserveManage sendReserveManage = sendReserveManageRepositoryPort.findByExternalEvent(notice.getExternalEvent()).orElse(null);

                if (sendReserveManage == null && countProductOrdersInit == initPositionPiecesWithoutDesist.size()) {
                    reserveCalculationInitDTO = calculateValuesReserve(notice, initPositionPieces, unforeseenPositionPieces);
                    if (reserveCalculationInitDTO != null) {
                        reserveCalculationPort.sendReserveCalculationAdmin(reserveCalculationInitDTO);
                        createSendReserveManage(notice.getExternalEvent());
                        response = true;
                    }
                }
            }

        } catch (Exception e) {
            log.info("sendDeletedTotalReserve: " + e);
        }

        return response;
    }

    public void createSendReserveManage(Integer externalEvent) throws ExceptionUtil {

        try {
            SendReserveManage sendReserve = new SendReserveManage()
                    .setExternalEvent(externalEvent)
                    .setInitCarSended(true)
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone());

            sendReserveManageRepositoryPort.save(sendReserve);

        } catch (Exception e) {
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATING_SEND_RESERVE, e.getMessage());
        }
    }

    public List<Integer> validateInitPositions(Integer externalEvent, boolean unforeseen) throws ExceptionUtil {
        try {
            return validatePositions(externalEvent, unforeseen);
        } catch (Exception e) {
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_VALIDATING_INIT_POSITIONS, e.getMessage());
        }
    }

    private List<Integer> validatePositions(Integer externalEvent, boolean unforeseen) {
        boolean countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen) == 1;

        List<Integer> listPosition;
        if (countInitPackage) {
            listPosition = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);
        } else {
            listPosition = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(externalEvent, unforeseen);
        }

        return listPosition;
    }

    public ReserveCalculationDTO calculateValuesReserve(Notice notice, List<Integer> positionInit, List<Integer> positionsUnforeseen) throws ExceptionUtil {

        ReserveCalculationDTO response = new ReserveCalculationDTO();
        List<ReserveCalculationTotalDTO> listInit = new ArrayList<>(0);
        List<ReserveCalculationTotalDTO> listUnforeseen = new ArrayList<>(0);

        try {

            if (!positionInit.isEmpty()) {
                ReserveCalculationTotalDTO reserveCalculationInit = new ReserveCalculationTotalDTO();
                reserveCalculationInit.setTotalRepuNeto(productOrderRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), true, positionInit, false));
                reserveCalculationInit.setTotalTotNeto(productOrderRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), false, positionInit, false));
                listInit.add(reserveCalculationInit);
            } else {
                listInit.add(new ReserveCalculationTotalDTO());
            }

            if (!positionsUnforeseen.isEmpty()) {
                ReserveCalculationTotalDTO reserveCalculationUnforeseen = new ReserveCalculationTotalDTO();
                reserveCalculationUnforeseen.setTotalRepuNeto(productOrderRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), true, positionsUnforeseen, true));
                reserveCalculationUnforeseen.setTotalTotNeto(productOrderRepositoryPort.totalPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), false, positionsUnforeseen, true));
                listUnforeseen.add(reserveCalculationUnforeseen);
            } else {
                listUnforeseen.add(new ReserveCalculationTotalDTO());
            }

            response.setNumeroAviso(notice.getExternalEvent());
            response.setPedidoInicial(listInit);
            response.setImprevistos(listUnforeseen);

        } catch (Exception e) {
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CALCULATING_VALUES_RESERVE, e.getMessage());
        }

        return response;
    }

    public List<Integer> validateInitPositionsWithoutDesist(Integer externalEvent, boolean unforeseen) {
        List<Integer> initPositionPieces = null;
        List<Integer> initPositionPiecesDesist = null;
        try {

            boolean countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen) == 1;

            if (countInitPackage) {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);
            } else {
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(externalEvent, unforeseen);
            }
            initPositionPiecesDesist = pieceRepositoryPort.findInitialPiecesByExternalWithOrders(externalEvent, unforeseen);

            if (!initPositionPiecesDesist.isEmpty()) {
                initPositionPieces.removeAll(initPositionPiecesDesist);
            }
        } catch (Exception e) {
            log.info("validateInitPositionsWithoutDesist: " + e);
        }
        return initPositionPieces;
    }

    public boolean validateReserveSura(String externalEvent) {

        boolean response = false;
        boolean validateInit = false;
        Long countProductOrdersInit = -1L;
        try {
            Notice notice = noticeRepositoryPort.findAllByExternalEvent(externalEvent).get(0);
            Insurer insurer = insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.INSURER_NOT_FOUND));

            log.info("condition Bolivar: " + insurer.getName().equalsIgnoreCase(Common.BOLIVAR));
            log.info("condition Sura: " + insurer.getName().equalsIgnoreCase(Common.SURA));

            if (insurer.getFlowReserveSura() && notice.isAuth() && insurer.getName().equalsIgnoreCase(Common.SURA)) {
                log.info("Inicia proceso Reserva Sura Validate");
                ReserveCalculationSuraDTO reserveCalculationInitDTO;
                List<Integer> initPositionPieces = validateInitPositionsSura(notice.getExternalEvent(), false);
                List<Integer> initPositionPiecesWithoutDesist = validateInitPositionsWithoutDesistSura(notice.getExternalEvent(), false);
                List<Integer> unforeseenPositionPieces = validateInitPositionsSura(notice.getExternalEvent(), true);

                if (!initPositionPiecesWithoutDesist.isEmpty()) {
                    countProductOrdersInit = productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(
                            notice.getExternalEvent(), initPositionPiecesWithoutDesist);
                }

                if (initPositionPieces.isEmpty()) {
                    Long countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(notice.getExternalEvent(), false);
                    validateInit = (countInitPackage > 0);
                }

                SendReserveManage sendReserveManage = sendReserveManageRepositoryPort.findByExternalEvent(notice.getExternalEvent()).orElse(null);

                if (sendReserveManage == null && (countProductOrdersInit == initPositionPiecesWithoutDesist.size() || validateInit)) {
                    reserveCalculationInitDTO = calculateValuesReserveSura(notice, initPositionPieces, unforeseenPositionPieces);
                    if (reserveCalculationInitDTO != null) {
                        reserveSuraPort.sendPiecesReserveAdminSura(reserveCalculationInitDTO);
                        createSendReserveManage(notice.getExternalEvent());
                        response = true;
                    }
                }
            }

        } catch (Exception e) {
            log.info("validateReserveSura: " + e);
        }

        return response;
    }

    public ReserveCalculationSuraDTO calculateValuesReserveSura(Notice notice, List<Integer> positionInit, List<Integer> positionsUnforeseen) {

        ReserveCalculationSuraDTO response = new ReserveCalculationSuraDTO();
        ReserveCalculationTotalSuraDTO reserveCalculationInit = new ReserveCalculationTotalSuraDTO();
        ReserveCalculationTotalSuraDTO reserveCalculationUnforeseen = new ReserveCalculationTotalSuraDTO();
        List<Integer> positionsFinal = new ArrayList<>(0);
        try {

            if (!positionInit.isEmpty()) {
                reserveCalculationInit =
                        productOrderRepositoryPort.totalGrossPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), true, positionInit, false);
                positionsFinal.addAll(positionInit);
            }

            if (!positionsUnforeseen.isEmpty()) {
                reserveCalculationUnforeseen =
                        productOrderRepositoryPort.totalGrossPriceOrdersByExternalEventAndEventId(notice.getExternalEvent(), true, positionsUnforeseen, true);
                positionsFinal.addAll(positionsUnforeseen);
            }

            List<ReserveRepuestosSuraDTO> listRepuestos = productOrderRepositoryPort.findPiecesOrdersByExternalEvent(notice.getExternalEvent(), true, positionsFinal);

            response.setNumeroAviso(notice.getExternalEvent());
            response.setPedidoInicial(reserveCalculationInit);
            response.setImprevistos(reserveCalculationUnforeseen);
            response.setRepuesto(listRepuestos);

        } catch (Exception e) {
            log.info("calculateValuesReserveSura: " + e);
        }
        return response;
    }

    public List<Integer> validateInitPositionsSura(Integer externalEvent, boolean unforeseen) {
        try {
            return validatePositions(externalEvent, unforeseen);
        } catch (Exception e) {
            log.info("validateInitPositionsSura: " + e);
            return Collections.emptyList();
        }
    }

    public List<Integer> validateInitPositionsWithoutDesistSura(Integer externalEvent, boolean unforeseen) {
        List<Integer> initPositionPieces = null;
        List<Integer> initPositionPiecesDesist = null;
        try {
            Long countInitPackage = noticeRepositoryPort.countAllByExternalEventAndUnforeseen(externalEvent, unforeseen);

            if (countInitPackage == 1)
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionTrue(externalEvent, unforeseen);
            else
                initPositionPieces = pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionFalse(externalEvent, unforeseen);

            initPositionPiecesDesist = pieceRepositoryPort.findInitialPiecesByExternalWithOrdersSura(externalEvent, unforeseen);

            if (!initPositionPiecesDesist.isEmpty()) {
                initPositionPieces.removeAll(initPositionPiecesDesist);
            }
        } catch (Exception e) {
            log.info("validateInitPositionsWithoutDesistSura: " + e);
        }
        return initPositionPieces;
    }

    public void sendSparePartInformationFollowUpDeleted(Long externalEvent, Integer position) {
        try {
            List<Integer> positions = new ArrayList<>();
            positions.add(position);
            Notice notice = noticeRepositoryPort.findAllByExternalEvent(externalEvent.toString()).get(0);

            List<SpareDetailToFollowUpDTO> listResponse = noticeRepositoryPort.findInfoFollowUpByExternalEventAndPositions(externalEvent, positions, true, false);
            List<SpareDetailToFollowUpDTO> listResponseNoAut = noticeRepositoryPort.findInfoFollowUpByExternalEventAndPositionsNoAut(externalEvent);

            if (!notice.isAuth()) {
                createRequestAndSendInformationFollowUp(externalEvent, listResponse);
            } else if (listResponseNoAut.isEmpty()) {
                createRequestAndSendInformationFollowUp(externalEvent, listResponse);
            } else {
                List<SpareDetailToFollowUpDTO> listFinal = new ArrayList<>(listResponse);
                listFinal.addAll(listResponseNoAut);
                createRequestAndSendInformationFollowUp(externalEvent, listFinal);
            }

        } catch (Exception e) {
            log.info("sendRepuInformationSeguimientoDeleted: " + e.getMessage() + e.getCause());
        }
    }

    public boolean createRequestAndSendInformationFollowUp(Long externalEvent, List<SpareDetailToFollowUpDTO> listDTO) {
        try {
            if (listDTO != null && !listDTO.isEmpty()) {
                SendSparesToFollowUPDTO sendInfoFollowUpDTO = new SendSparesToFollowUPDTO(externalEvent, listDTO);
                return followUpPort.sendDataToSFollowUp(sendInfoFollowUpDTO);

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("createRequestAndSendInformationSeguimiento: " + e.getMessage() + e.getCause());
        }
        return false;
    }

}
