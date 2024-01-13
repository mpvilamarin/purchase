package com.subocol.manage.purchase.domain.models;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency{

	private Long id;

	private Integer currencyId;

	private String prefix;

	private String description;

	private Integer divide;

	private Double fixedRate;

	private Long countryId;

}