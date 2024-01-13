package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPurchaseInvoiceDTO {
	
	private Double grossValue;
	private String providerNit;
	private Integer taxValue;
	private Double totalValue;
	private String currency;
	private String companyNameProvider;
	private Long orderNumber;
	private Long externalEvent;
	private String plate;
	private String claimNumber;
	private Long insuranceNumber;
	private List<OrderPurchaseInvoiceReplacementDTO> replacement = new ArrayList<OrderPurchaseInvoiceReplacementDTO>();


}
