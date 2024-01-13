package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;


@Getter
@Setter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuotation {

	private Long id;

	private Quotation quotation;
//	private Long quotationId;

	private String status;

	private Integer amount;

	private String quality;

	private Timestamp dispatchDate;

	private String guide;

	private String comment;

	private String reference;

	private String description;

	private Double grossPrice;

	private Double netPrice;	

	private Timestamp acceptDate;

	private Boolean importer;

	private Integer deliveryTime;

	private Boolean purchase;

	private String suggestedReference;

	private Double valueExtraCost;

	private Boolean extraCost;

	private Boolean maxDeliveryDays;

	private Integer valueMaxDeliveryDays;

	private Double discountAdditional;

	private Double discountBrand;

	private Double discountCampaigns;

	private boolean auth;

	private int position;

	private Timestamp promiseDelivery;

	private Boolean active;

	private Double discountManual;

	private Long manualPurchaseId;

	private Boolean winner;

	private String userName;

	private Boolean maxCostPiece;

	private boolean deleted;

	private boolean purchaseSubsidiary;

	private Boolean sendValuationQuotation;

	private Boolean sendValuationPurchase;
	
}
