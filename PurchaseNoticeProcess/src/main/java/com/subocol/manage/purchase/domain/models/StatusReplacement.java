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
public class StatusReplacement {

	private Long id;

	private String externalEvent;

	private String seller;

	private Timestamp dateOrder;

	private String provider;

	private String email;

	private String phone;

	private Timestamp approvedDate;

	private String providerObservation;

	private Integer quantityParts;

	private String subsidiary;

	private String emailSubsidiary;

	private String phoneSubsidiary;

	private Set<StatusParts> statusParts = new HashSet<>();
	
}
