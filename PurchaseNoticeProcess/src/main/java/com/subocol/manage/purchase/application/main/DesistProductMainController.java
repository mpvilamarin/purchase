package com.subocol.manage.purchase.application.main;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.annotations.MainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.DesistProduct;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.DesistDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@MainController
public class DesistProductMainController {

    private final DesistProduct desistProduct;

    public DesistProductMainController(DesistProduct desistProduct) {
        this.desistProduct = desistProduct;
    }

    public ResponseDTO desistProduct(DesistDTO desistDTO) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            return desistProduct.desistProduct(desistDTO);
        } catch (ExceptionUtil e) {
            return responseDTO.setStatus(e.getCode()).setMessage(e.getMessage())
                    .setDate(LocalDateTime.now().toString()).setSuccess(false).setData(null);
        } catch (Exception e) {
            return responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage())
                    .setDate(LocalDateTime.now().toString()).setSuccess(false).setData(e.getMessage());
        }
    }
}
