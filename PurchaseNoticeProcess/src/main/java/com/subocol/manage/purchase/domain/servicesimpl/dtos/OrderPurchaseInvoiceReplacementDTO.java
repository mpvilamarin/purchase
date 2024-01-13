package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchaseInvoiceReplacementDTO {
	
	private String replacementRef;
	private String replacementName;
	private Integer orderedQuantity;
	private Double replacementValue;
	private Long replacementId;
}
