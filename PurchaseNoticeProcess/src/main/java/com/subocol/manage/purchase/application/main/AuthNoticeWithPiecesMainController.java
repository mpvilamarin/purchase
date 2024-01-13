package com.subocol.manage.purchase.application.main;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.annotations.MainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.servicesimpl.AuthNoticeWithPieces;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@MainController
public class AuthNoticeWithPiecesMainController {

    private final AuthNoticeWithPieces authNoticeWithPieces;

    public AuthNoticeWithPiecesMainController(AuthNoticeWithPieces authNoticeWithPieces) {
        this.authNoticeWithPieces = authNoticeWithPieces;
    }


    public ResponseDTO authNotice(AuthNoticePiecesDTO authNoticePieces) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            return authNoticeWithPieces.auth(authNoticePieces);
        } catch (ExceptionUtil e) {
            return responseDTO.setStatus(e.getCode()).setMessage(e.getMessage())
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone().toString()).setSuccess(false).setData(null);
        } catch (Exception e) {
            return responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage())
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone().toString()).setSuccess(false).setData(e.getMessage());
        }
    }


}
