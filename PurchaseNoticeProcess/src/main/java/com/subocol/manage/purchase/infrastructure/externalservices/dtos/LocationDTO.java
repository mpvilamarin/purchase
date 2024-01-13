package com.subocol.manage.purchase.infrastructure.externalservices.dtos;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class LocationDTO 
{
	private Long id;

	private String address;

	private CityDTO city;

}
