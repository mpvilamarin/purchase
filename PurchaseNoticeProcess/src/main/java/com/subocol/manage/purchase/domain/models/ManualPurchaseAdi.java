package com.subocol.manage.purchase.domain.models;

import lombok.*;

import lombok.experimental.Accessors;


@Setter
@Getter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualPurchaseAdi {

	private Long idRegister;

	private String irs;

	private String pieces;

	private String brand;

	private String line;

	private String version;

	private String vin;

	private Long event;

	private int quantity;

	private String quality;

	private String homologatedName;

	private String group;

	private String subgroup;

	private String suggestedReference;

	private int position;

	private String type;

	private boolean manualPurchase;

	private Long eventId;

}
