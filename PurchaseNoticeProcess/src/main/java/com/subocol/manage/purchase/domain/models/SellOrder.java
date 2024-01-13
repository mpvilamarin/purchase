package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class SellOrder 
{

	private Long id;

	private Order order;

	private Timestamp creationDate;

	private Timestamp lastUpdateDate;

	private Double subtotal;

	private Double iva;

	private Double total;

	private String pdfUrl;

	private Set<SellOrderDetail> details = new HashSet<>();

	public void addDetail(SellOrderDetail sellOrderDetail) 
	{
		if (this.details == null) 
			this.details = new HashSet<>();
		this.details.add(sellOrderDetail);
	}
}