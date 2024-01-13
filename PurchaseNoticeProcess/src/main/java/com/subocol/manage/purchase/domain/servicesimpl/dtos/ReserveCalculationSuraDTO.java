package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import java.util.List;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReserveCalculationSuraDTO {

	private Integer numeroAviso;
	private ReserveCalculationTotalSuraDTO pedidoInicial;
	private ReserveCalculationTotalSuraDTO imprevistos;
	private List<ReserveRepuestosSuraDTO> repuesto;
	
}
