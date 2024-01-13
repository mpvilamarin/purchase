package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ReserveCalculationDTO {

	private Integer numeroAviso;
	private List<ReserveCalculationTotalDTO> pedidoInicial;
	private List<ReserveCalculationTotalDTO> imprevistos;
	
}