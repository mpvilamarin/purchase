package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ReserveCalculationTotalDTO {

    private Double totalRepuNeto;
    private Double totalTotNeto;

    public ReserveCalculationTotalDTO() {
        this.totalRepuNeto = 0D;
        this.totalTotNeto = 0D;
    }
}