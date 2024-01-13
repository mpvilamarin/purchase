package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.google.gson.Gson;
import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.AlertPiecesValuationPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Adapter
public class AlertPiecesValuationAdapter implements AlertPiecesValuationPort {

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;

    public AlertPiecesValuationAdapter(RestTemplate restTemplate, SingletonProperties propertiesBean) {
        this.restTemplate = restTemplate;
        this.propertiesBean = propertiesBean;
    }

    public String serviceAlertPiecesValuation(NoticeValuationDTO noticeValuationDTO) {
        String result = "";
        try {
            log.info("Start request valuation service");
            log.info("Request DTO: " + new Gson().toJson(noticeValuationDTO));
            String authCredentials = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES);

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(authCredentials);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<NoticeValuationDTO> request = new HttpEntity<>(noticeValuationDTO, headers);
            String url = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.VALUATION_QUOTE_PIECES);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            result = response.getBody();
            log.info("Service AlertPiecesValoration response: " + result);

        } catch (RestClientException e) {
            log.error("El servicio de valoración respondió: " + e.getMessage() + ", por favor verifique los datos de la petición");
            return null;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
