package com.subocol.manage.purchase.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private boolean success;
    private int status;
    private String date;
    private String message;
    private Object data;
}