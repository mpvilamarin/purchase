package com.subocol.manage.purchase.application.main;

import com.subocol.manage.purchase.application.dtos.DeletePiecesManualPurchaseDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.annotations.MainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.servicesimpl.DeletePieces;
import org.springframework.http.HttpStatus;

@MainController
public class DeletePiecesMainController {

    private final DeletePieces deletePieces;

    public DeletePiecesMainController(DeletePieces de) {
        this.deletePieces = de;
    }

    public ResponseDTO deletePieces(DeletePiecesManualPurchaseDTO deletePiecesDTO) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {

            deletePieces.deletePieces(deletePiecesDTO);

            return responseDTO.setStatus(HttpStatus.OK.value())
                    .setSuccess(true)
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone().toString())
                    .setMessage("Pieces deleted successfully");

        } catch (ExceptionUtil e) {
            return responseDTO.setStatus(e.getCode()).setMessage(e.getMessage())
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone().toString()).setSuccess(false).setData(null);
        } catch (Exception e) {
            return responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage())
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone().toString()).setSuccess(false).setData(e.getMessage());
        }
    }


}
