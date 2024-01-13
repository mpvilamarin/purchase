package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
public class SpareDetailToFollowUpDTO {

	private Integer posicion;
	private Integer cantidad;
	private boolean esCotizado;
	private boolean deleted;
	private Long idStatusParts;
	
	
	
	public SpareDetailToFollowUpDTO(Integer posicion, Integer cantidad, boolean esCotizado, boolean deleted) {
		this.posicion = posicion;
		this.cantidad = cantidad;
		this.esCotizado = esCotizado;
		this.deleted = deleted;
		this.idStatusParts = null;
	}
	
	
	public SpareDetailToFollowUpDTO(Integer posicion, Integer cantidad, boolean esCotizado, boolean deleted, Long idStatusParts) {
		this.posicion = posicion;
		this.cantidad = cantidad;
		this.esCotizado = esCotizado;
		this.deleted = deleted;
		this.idStatusParts = idStatusParts;
	}
}