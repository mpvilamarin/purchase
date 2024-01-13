package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Provider 
{

	private Long id;

	private String nit;

	private String name;

	private String contactName;

	private String phone;

	private String email;

	private Boolean active;

	private Long locationExternalId;

	private char typeProvider;

	private Float negotiatedDiscount;

	private String providerClassification;

	private Double intermediationDiscount;

	private Integer contactId;

	private Long devolutionTime;

	private Boolean authorizedNetwork;

	private Double minimumAmount;

	private Boolean invoiceSubsidiary;

	private String emailAccounting;

	private String emailElectronicInvoice;								

	private Integer daysIntermediationMostrador;

}