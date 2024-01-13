package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Insurer {

	private Long id;

	private String name;

	private Long insurerId;

	private Long countryId;

	private Boolean sdkActive;

	private Boolean flowIdJob;

	private String priceToUse;

	private Boolean sdkDmsSubsidiary;

	private Boolean sdkDmsOrders;

	private Boolean irsParameterManual;

	private Boolean unregisteredSubsidiary;

	private boolean multimedia;

	private Boolean newSuggestedReferenceParameter;

    private Boolean prioritizePriceList;

    private Boolean usePriceList;

    private Boolean useSuggestedReference;

    private Integer lengthReference;

    private Boolean dontAllowReferenceEmpty;

    private Boolean useGrossPriceCostoverrun;

    private Boolean calculateValueCostoverrun;

	private Boolean noHomologueManual;

	private Boolean totManual;

	private Boolean allowMaxCostPiece;

	private Boolean useOrbikaValuation;

	private Boolean ignoreMaxcostpieceMM;

	private Long daysUpdateSuggestedReference;

	private boolean decimals;

	private Boolean flowReserveBolivar;

	private Boolean sendOrderEmailWinner;

	private Boolean sendOrderFact;

	private Boolean assignWorkshopId;

	private Boolean requiredReference;

	private Boolean modalReferenceSwitch;

	private Boolean qualityPieceQuotation;

	private Boolean flowReserveSura;

    private Integer cantRefToShow;

}
