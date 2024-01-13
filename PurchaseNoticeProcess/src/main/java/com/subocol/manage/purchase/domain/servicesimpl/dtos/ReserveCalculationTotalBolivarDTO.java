package com.subocol.manage.purchase.domain.servicesimpl.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ReserveCalculationTotalBolivarDTO {

    private Double totalRepuNeto;
    private Double totalTotNeto;

    public ReserveCalculationTotalBolivarDTO() {
        this.totalRepuNeto = 0D;
        this.totalTotNeto = 0D;
    }
}
