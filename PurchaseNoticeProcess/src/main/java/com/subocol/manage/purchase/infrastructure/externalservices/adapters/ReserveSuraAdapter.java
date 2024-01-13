package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveSuraPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationSuraDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class ReserveSuraAdapter implements ReserveSuraPort {

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;

    @Override
    public String sendPiecesReserveAdminSura(ReserveCalculationSuraDTO reserveCalculationDTO) {
        String result = "";
        try {
            log.info("INICIA LLAMADO SERVICIO ADMIN RESERVA SURA");
            log.info("ReserveCalculationSuraDTO: "+reserveCalculationDTO.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ReserveCalculationSuraDTO> request = new HttpEntity<>(reserveCalculationDTO, headers);
            String url = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_SURA);

            log.info(url);
            restTemplate.postForEntity(url, request, String.class);
            result = HttpStatus.OK.toString();
        } catch (Exception e) {
            e.getCause();
            log.error(e.getMessage());
        }

        return result;
    }
}
