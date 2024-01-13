package com.subocol.manage.purchase.infrastructure.externalservices.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDTO 
{
	private Long id;

	private String name;

	private String state;

	private String isoCode;

	private RegionDTO region;
}
