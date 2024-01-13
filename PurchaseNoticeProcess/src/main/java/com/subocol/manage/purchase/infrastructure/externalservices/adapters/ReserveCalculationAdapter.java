package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveCalculationPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Adapter
@Slf4j
public class ReserveCalculationAdapter implements ReserveCalculationPort {

    private final SingletonProperties singletonProperties;

    public ReserveCalculationAdapter(SingletonProperties singletonProperties) {
        this.singletonProperties = singletonProperties;
    }

    @Override
    public boolean sendReserveCalculationAdmin(ReserveCalculationDTO reserveCalculationDTO) {

        try {
            log.info("INICIA LLAMADO SERVICIO ADMIN RESERVA");
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ReserveCalculationDTO> request = new HttpEntity<>(reserveCalculationDTO, headers);
            String url = singletonProperties.getCurrentPropertyByKey(PropertiesConstants.ADMIN_BILLING_CALCULATION);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            log.info("SALIDA JSON: \n" + json);
            restTemplate.put(url, request, String.class);
            return true;
        } catch (Exception e) {
            e.getCause();
            log.error(e.getMessage());
            return false;
        }


    }
}
