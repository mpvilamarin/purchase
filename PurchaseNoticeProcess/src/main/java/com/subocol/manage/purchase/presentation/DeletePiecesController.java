package com.subocol.manage.purchase.presentation;

import com.subocol.manage.purchase.application.dtos.DeletePiecesManualPurchaseDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.DeletePiecesMainController;
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
public class DeletePiecesController {

    @Autowired
    private DeletePiecesMainController deletePiecesMainController;

    @PostMapping("/delete_pieces")
    public ResponseEntity<ResponseDTO> deletePieces(@RequestBody DeletePiecesManualPurchaseDTO deletePiecesDTO) {

        ResponseDTO responseDTO = deletePiecesMainController.deletePieces(deletePiecesDTO);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

}
 