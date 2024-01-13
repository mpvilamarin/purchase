package com.subocol.manage.purchase.domain.models;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tax implements Serializable
{

	private Long id;

	private Integer taxIdDms;

	private String description;

	private Integer percentage;

	private Integer type;

	private Long countryId;

	private String taxName;

}