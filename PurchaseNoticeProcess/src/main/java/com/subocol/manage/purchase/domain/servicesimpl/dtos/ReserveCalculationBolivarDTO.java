package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReserveCalculationBolivarDTO {

    private Integer numeroAviso;
    private List<ReserveCalculationTotalBolivarDTO> pedidoInicial;
    private List<ReserveCalculationTotalBolivarDTO> imprevistos;
}
