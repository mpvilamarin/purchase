package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.FollowUpPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class FollowUpAdapter implements FollowUpPort {

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;

    @Override
    public boolean sendDataToSFollowUp(SendSparesToFollowUPDTO sendSparesToFollowUPDTO) {
        try {
            log.info("Start follow-up service consumption");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<SendSparesToFollowUPDTO> request = new HttpEntity<>(sendSparesToFollowUPDTO, headers);
            String url = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.FOLLOWUP_SEND_INFORMATION);
            log.info(url);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request);
            log.info("JSON send to follow-up: " + json);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String result = response.getBody();
            log.info("response follow-up services: " + result);

            return true;
        } catch (Exception e) {

            e.getCause();
            log.error(e.getMessage() + e.getStackTrace());
            return false;
        }
    }
}
