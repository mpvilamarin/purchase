package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class MailOrderCreateDTO {

    private String aviso;

    private String subsidiaryName;

    private String link;

    private String email;

    private Long subsidiaryId;

    private Long orderId;

    private Boolean manual;

    private String plate;

    private String insurerName;

}
