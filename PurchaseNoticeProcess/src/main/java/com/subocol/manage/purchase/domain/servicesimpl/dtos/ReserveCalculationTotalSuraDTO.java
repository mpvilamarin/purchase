package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ReserveCalculationTotalSuraDTO {

	private Double totalRepuestos;
	private Double precioTotalIva;
	private Double valorTotalDescuento;
	
	public ReserveCalculationTotalSuraDTO() {
		this.totalRepuestos = 0D;
		this.precioTotalIva = 0D;
		this.valorTotalDescuento = 0D;
	}
	
	public ReserveCalculationTotalSuraDTO(Double total, Double totalIva, Double totalDescuento) {
		this.totalRepuestos = (total != null) ? Math.round(total * Math.pow(10, 2)) / Math.pow(10, 2) : 0.0;
		this.precioTotalIva = (totalIva != null) ? Math.round(totalIva * Math.pow(10, 2)) / Math.pow(10, 2) : 0.0;
		this.valorTotalDescuento = (totalDescuento != null) ? Math.round(totalDescuento * Math.pow(10, 2)) / Math.pow(10, 2) : 0.0;

	}
}
