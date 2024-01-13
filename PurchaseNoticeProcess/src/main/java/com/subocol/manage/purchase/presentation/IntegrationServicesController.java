package com.subocol.manage.purchase.presentation;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.IntegrationServicesMainController;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ExternalIntegrationDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class IntegrationServicesController {

    @Autowired
    private IntegrationServicesMainController integrationServicesMainController;

    @PostMapping("/integration_services")
    public ResponseEntity<ResponseDTO> integrationServices(@Valid @RequestBody ExternalIntegrationDTO externalIntegrationDTO, BindingResult bindingResult) {

        ResponseEntity<ResponseDTO> result = getResponseEntity(bindingResult);
        if (result != null)
            return result;

        ResponseDTO responseDTO = integrationServicesMainController.integrationServices(externalIntegrationDTO);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    private static ResponseEntity<ResponseDTO> getResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            String result1 = bindingResult.getAllErrors().parallelStream()
                    .collect(StringBuilder::new, (x, y) -> x.append(y.getDefaultMessage()), (a, b) -> a.append(" ").append(b))
                    .toString();

            return new ResponseEntity<>(new ResponseDTO()
                    .setMessage(result1)
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .setSuccess(false), HttpStatus.BAD_REQUEST);
        }

        return null;
    }
}
