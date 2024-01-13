package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/** Entity representing order data */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Accessors(chain = true)
public class Order 
{

	private Long id;

	private Timestamp date;

	private Subsidiary subsidiary;
	//private Long subsidiaryId;

	private String status;

	private String workshop;

	private Integer time;

//	private Long noticeId;

	private Integer priority;

	private Set<ProductOrder> products;

	//private SellOrder sellOrder;

	private String reference;

	private String comment;

	private Timestamp workshopDeliveryDate;

	private String documentUrl;

	private Quotation quotation;

	private Integer orderIdDms;

	private Integer orderPurchaseDms;

	private Long orderPurchaseChile;

	private String orderIdSubocol;

	private Integer billingServiceId;

	private Boolean unforeseen;

	private BigDecimal repairOrder;

	private Integer purchaseTypeId;

	private Notice notice;

	public void addProduct(ProductOrder product) {
		if (this.products == null) {
			this.products = new HashSet<>();
		}
		this.products.add(product);
	}

}