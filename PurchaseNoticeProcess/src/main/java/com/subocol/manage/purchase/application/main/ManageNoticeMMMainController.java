package com.subocol.manage.purchase.application.main;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.annotations.MainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeMM;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@MainController
@RequiredArgsConstructor
public class ManageNoticeMMMainController {

    private final ManageNoticeMM manageNoticeMM;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO manageNoticeMM(Long noticeId) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            boolean manageNotice = manageNoticeMM.manageNoticeByNoticeId(noticeId, Boolean.TRUE);

            if (Boolean.TRUE.equals(manageNotice)) {
                return responseDTO.setStatus(HttpStatus.OK.value()).setMessage(MessageResponse.REQUEST_IS_SUCCESS)
                        .setDate(LocalDateTime.now().toString()).setSuccess(true).setData(null);
            } else {
                return responseDTO.setStatus(HttpStatus.BAD_REQUEST.value()).setMessage(MessageResponse.REQUEST_NOT_SUCCESS)
                        .setDate(LocalDateTime.now().toString()).setSuccess(false);
            }
        } catch (ExceptionUtil e) {
            return responseDTO.setStatus(e.getCode()).setMessage(e.getMessage())
                    .setDate(LocalDateTime.now().toString()).setSuccess(false).setData(null);
        } catch (Exception e) {
            return responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage())
                    .setDate(LocalDateTime.now().toString()).setSuccess(false).setData(e.getMessage());
        }
    }


}
