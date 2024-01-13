package com.subocol.manage.purchase.presentation;

import com.subocol.manage.purchase.application.dtos.ManageNoticeMMDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.ManageNoticeMMMainController;
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
public class ManageNoticeMMController {

    @Autowired
    private ManageNoticeMMMainController manageNoticeMMMainController;

    @PostMapping("/managenotice/mm")
    public ResponseEntity<ResponseDTO> manageNoticeMM(@RequestBody ManageNoticeMMDTO manageNoticeMMDTO) {
        ResponseDTO responseDTO = manageNoticeMMMainController.manageNoticeMM(manageNoticeMMDTO.getNoticeId());
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

}
 