package com.subocol.manage.purchase.application.main;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.annotations.MainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ExternalIntegrationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@MainController
@RequiredArgsConstructor
public class IntegrationServicesMainController {

    private final Integrations integrations;

    public ResponseDTO integrationServices(ExternalIntegrationDTO externalIntegrationDTO) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            return integrations.integrationServicesExposed(externalIntegrationDTO.getNoticeId(), externalIntegrationDTO.getInsurerId(), externalIntegrationDTO.getOrderIds());
        } catch (ExceptionUtil e) {
            return responseDTO.setStatus(e.getCode()).setMessage(e.getMessage())
                    .setDate(LocalDateTime.now().toString()).setSuccess(false).setData(null);
        } catch (Exception e) {
            return responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage())
                    .setDate(LocalDateTime.now().toString()).setSuccess(false).setData(e.getMessage());
        }
    }

}
