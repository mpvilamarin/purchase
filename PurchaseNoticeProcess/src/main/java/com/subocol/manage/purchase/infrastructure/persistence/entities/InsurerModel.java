package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_INSURERS, schema = DataBase.Constant.SCH_PARAMETERS)
public class InsurerModel {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "insurer_id")
    private Long insurerId;

    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "sdk_active")
    private Boolean sdkActive;

    @Column(name = "flow_idjob")
    private Boolean flowIdJob;

    @Column(name = "price_to_use")
    private String priceToUse;

    @Column(name = "sdk_dms_subsidiary")
    private Boolean sdkDmsSubsidiary;

    @Column(name = "sdk_dms_orders")
    private Boolean sdkDmsOrders;

    @Column(name = "irs_parameter_manual")
    private Boolean irsParameterManual;

    @Column(name = "unregistered_subsidiary")
    private Boolean unregisteredSubsidiary;

    @Column(name = "multimedia")
    private boolean multimedia;

    @Column(name = "use_new_suggested_reference")
    private Boolean newSuggestedReferenceParameter;

    @Column(name = "prioritize_price_list")
    private Boolean prioritizePriceList;

    @Column(name = "use_price_list")
    private Boolean usePriceList;

    @Column(name = "use_suggested_reference")
    private Boolean useSuggestedReference;

    @Column(name = "length_reference")
    private Integer lengthReference;

    @Column(name = "dont_allow_reference_empty")
    private Boolean dontAllowReferenceEmpty;

    @Column(name = "use_grossprice_costoverrun")
    private Boolean useGrossPriceCostoverrun;

    @Column(name = "calculate_value_costoverrun")
    private Boolean calculateValueCostOverrun;

    @Column(name = "no_homologate_manual")
    private Boolean noHomologueManual;

    @Column(name = "tot_manual")
    private Boolean totManual;

    @Column(name = "allow_max_cost_piece")
    private Boolean allowMaxCostPiece;

    @Column(name = "use_orbika_valoration")
    private Boolean useOrbikaValuation;

    @Column(name = "ignore_maxcostpiece_mm")
    private Boolean ignoreMaxCostPieceMM;

    @Column(name = "days_update_suggestedreference")
    private Long daysUpdateSuggestedReference;

    @Column(name = "decimals")
    private boolean decimals;

    @Column(name = "flow_reserve_bolivar")
    private Boolean flowReserveBolivar;

    @Column(name = "send_order_email_winner")
    private Boolean sendOrderEmailWinner;

    @Column(name = "send_order_fact")
    private Boolean sendOrderFact;

    @Column(name = "assign_workshop_id")
    private Boolean assignWorkshopId;

    @Column(name = "required_reference")
    private Boolean requiredReference;

    @Column(name = "modal_reference_switch")
    private Boolean modalReferenceSwitch;

    @Column(name = "quality_piece_quotation")
    private Boolean qualityPieceQuotation;

    @Column(name = "flow_reserve_sura")
    private Boolean flowReserveSura;

    @Column(name = "cant_ref_to_show")
    private Integer cantRefToShow;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "insurer_id")
    private Set<ReplacementQualityModel> replacementQuality;

}
