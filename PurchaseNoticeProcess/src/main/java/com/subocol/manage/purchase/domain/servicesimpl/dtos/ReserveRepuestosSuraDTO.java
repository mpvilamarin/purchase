package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@ToString
public class ReserveRepuestosSuraDTO {

	private String codigoPieza;
	private Integer posicionRepuesto;
	private String nombrePieza;
	private String codReferenciaPieza;
	private Integer cantidadPiezas;
	private Double valorUnitario;
	private Double iva;
	private Integer porcentajeIva;
	private String codigoOrigen;
	private String snImprevisto;
	private Double porcentajeDescuento;
	private String codProveedorPieza;
	
	
	public ReserveRepuestosSuraDTO(String codigoPieza, Integer posicionRepuesto, String nombrePieza, String codReferenciaPieza, Integer cantidadPiezas, 
			Double valorUnitario, Double iva, Double porcentajeIva, String codigoOrigen, boolean snImprevisto, Double porcentajeDescuento, 
			String codProveedorPieza, Double subtotal) {
		this.codigoPieza = codigoPieza;
		this.posicionRepuesto = posicionRepuesto;
		this.nombrePieza = nombrePieza;
		this.codReferenciaPieza = codReferenciaPieza;
		this.cantidadPiezas = cantidadPiezas;
		this.valorUnitario = Math.round(valorUnitario * Math.pow(10, 2)) / Math.pow(10, 2);
		if(subtotal==null || subtotal==0 ){
			this.iva = 0D;
			this.porcentajeIva =0;
		}
		else {
			this.iva = Math.round((iva/subtotal) * Math.pow(10, 2)) / Math.pow(10, 2);
			this.porcentajeIva = (int) Math.round(porcentajeIva/subtotal);
		}
		this.codigoOrigen = codigoOrigen;
		this.porcentajeDescuento = Math.round(porcentajeDescuento * Math.pow(10, 2)) / Math.pow(10, 2);
		this.codProveedorPieza = codProveedorPieza;
		if(snImprevisto) {
			this.snImprevisto = "S";
		}else {
			this.snImprevisto = "N";
		}
	}
}
