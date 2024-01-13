package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import java.util.List;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendSparesToFollowUPDTO {
	
	private Long idAviso;
	private List<SpareDetailToFollowUpDTO> repuestos;

}