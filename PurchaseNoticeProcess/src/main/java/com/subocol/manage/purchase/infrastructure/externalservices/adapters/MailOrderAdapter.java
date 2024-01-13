package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.MailOrderPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 29/06/2023
 */
@Adapter
public class MailOrderAdapter implements MailOrderPort {

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;

    public MailOrderAdapter(RestTemplate restTemplate, SingletonProperties propertiesBean) {
        this.restTemplate = restTemplate;
        this.propertiesBean = propertiesBean;
    }

    @Override
    public boolean sendOrderCreateNotification(List<MailOrderCreateDTO> mailData) {

        String requestUrl = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.NOTIFICATION_BASE_PATH) +
                "/api/notification/ordercreated";

        HttpEntity<List<?>> request = new HttpEntity<>(mailData, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);

        return response.getStatusCode().is2xxSuccessful();

    }

}
