package com.subocol.manage.purchase.presentation;

import com.subocol.manage.purchase.application.dtos.ManageNoticeCCDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.ManageNoticeCCMainController;
import com.subocol.manage.purchase.domain.models.Desist;
import com.subocol.manage.purchase.domain.servicesimpl.DesistProduct;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.DesistDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class ManageNoticeCCController {

    @Autowired
    private ManageNoticeCCMainController manageNoticeCCMainController;

    @PostMapping("/managenotice/cc")
    public ResponseEntity<ResponseDTO> manageNoticeCC(@RequestBody ManageNoticeCCDTO manageNoticeCCDTO) {
        ResponseDTO responseDTO = manageNoticeCCMainController.manageNoticeCC(manageNoticeCCDTO);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

}
