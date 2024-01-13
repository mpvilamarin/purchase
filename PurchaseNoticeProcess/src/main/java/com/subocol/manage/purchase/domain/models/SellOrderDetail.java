package com.subocol.manage.purchase.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@NoArgsConstructor
public class SellOrderDetail 
{
	private Long id;

	private String reference;

	private String description;

	private Double unitPrice;

	private Integer amount;

	private Double total;

	private Double grossPrice;

	private Double discount;

	private Timestamp promiseDelivery;

	private String comment;

	private SellOrder sellOrder;
//	private Long sellOrderId;

	private Integer positionPiece;
}