package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExternalIntegrationDTO {

    @NotNull(message = "El campo 'noticeId' no puede ser nulo o estar vacio.")
    private Long noticeId;
    @NotNull(message = "El campo 'insurerId' no puede ser nulo o estar vacio.")
    private Long insurerId;

    private List<Long> orderIds;

}
