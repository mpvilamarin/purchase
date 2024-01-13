package com.subocol.manage.purchase.infrastructure.externalservices.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDTO 
{
	private Long id;
	private String name;
	private CountryDTO country;
}
