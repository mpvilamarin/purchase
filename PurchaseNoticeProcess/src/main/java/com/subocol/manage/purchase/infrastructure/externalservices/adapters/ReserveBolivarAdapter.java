package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveBolivarPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationBolivarDTO;
import org.springframework.web.client.RestTemplate;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.AlertPiecesValuationPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_EXTERNAL_SERVICE_RESERVE_BOLIVAR;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_SEND_RESERVE_BOLIVAR;

@Slf4j
@Adapter
public class ReserveBolivarAdapter implements ReserveBolivarPort {

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;

    public ReserveBolivarAdapter(RestTemplate restTemplate, SingletonProperties propertiesBean) {
        this.restTemplate = restTemplate;
        this.propertiesBean = propertiesBean;
    }

    public String sendReserveCalculationAdmin(ReserveCalculationBolivarDTO reserveCalculationBolivarDTO) {
        try {
            log.info("INICIA LLAMADO SERVICIO ADMIN RESERVA");
            log.info("reserveCalculationBolivarDTO: "+reserveCalculationBolivarDTO.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ReserveCalculationBolivarDTO> request = new HttpEntity<>(reserveCalculationBolivarDTO, headers);
            String url = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_BOLIVAR);
            log.info(url);
            restTemplate.put(url, request, String.class);
            log.info("LLAMADO SERVICIO ADMIN RESERVA COMPLETADO");
            return HttpStatus.OK.toString();
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
