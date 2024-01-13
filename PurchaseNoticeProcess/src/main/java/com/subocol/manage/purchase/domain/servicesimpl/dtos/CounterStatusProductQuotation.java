package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounterStatusProductQuotation {

	private Long id;
	private Long totalProducts;
	private Long omittedProducts;
	private Long quotedProducts;
	private Long rejectedQuotedProducts;
	private Long acceptedProducts;
	private Long alertAndWinnerProducts;

	public CounterStatusProductQuotation(Long id, Long totalProducts, Long omittedProducts, Long quotedProducts, Long rejectedQuotedProducts, Long acceptedProducts) {
		this.id = id;
		this.totalProducts = totalProducts;
		this.omittedProducts = omittedProducts;
		this.quotedProducts = quotedProducts;
		this.rejectedQuotedProducts = rejectedQuotedProducts;
		this.acceptedProducts = acceptedProducts;
	}
}
