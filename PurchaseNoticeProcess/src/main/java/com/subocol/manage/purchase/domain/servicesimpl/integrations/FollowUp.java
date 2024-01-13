package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.ThrowingConsumer;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.ports.externalservices.FollowUpPort;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductOrderRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowUp {

    private final FollowUpPort followUpPort;
    private final NoticeRepositoryPort noticeRepositoryPort;
    private final ProductOrderRepositoryPort productOrderRepositoryPort;

    public boolean sendPurchasesToFollowUp(Long externalEvent, Set<Order> orders) throws ExceptionUtil {

        try {

            log.info("Start process: Send purchases to follow up");
            List<Integer> positions = new ArrayList<>();
            orders.forEach(ThrowingConsumer.wrapExceptions(order -> order.getProducts().forEach(productOrder -> positions.add(productOrder.getPositionPiece()))));

            List<SpareDetailToFollowUpDTO> listResponse = noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositions(externalEvent, positions, false, false);
            List<SpareDetailToFollowUpDTO> listResponseNoAut = noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositionsNoAuth(externalEvent);

            if (listResponseNoAut.isEmpty()) {
                requestToFollowUp(externalEvent, listResponse);
            } else {
                List<SpareDetailToFollowUpDTO> listFinal = new ArrayList<>(listResponse);
                listFinal.addAll(listResponseNoAut);
                requestToFollowUp(externalEvent, listFinal);
            }
            return true;
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public Boolean requestToFollowUp(Long externalEvent, List<SpareDetailToFollowUpDTO> listDTO) throws ExceptionUtil {


        if (listDTO != null && !listDTO.isEmpty()) {
            SendSparesToFollowUPDTO sendSparesToFollowUPDTO = new SendSparesToFollowUPDTO(externalEvent, listDTO);
            followUpPort.sendDataToSFollowUp(sendSparesToFollowUPDTO);
            return true;
        }

        return false;

    }

    public boolean sendPurchasesToFollowUpDesist(Long externalEvent, List<Long> productorderId) {
        try {

            List<SpareDetailToFollowUpDTO> listResponse = productOrderRepositoryPort.findSpareToFollowUpByExternalEventAndPositionsDesist(productorderId);
            requestToFollowUp(externalEvent, listResponse);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("sendDesistRepuInformationSeguimiento: " + e.getMessage() + e.getCause());
        }
        return true;
    }

}
