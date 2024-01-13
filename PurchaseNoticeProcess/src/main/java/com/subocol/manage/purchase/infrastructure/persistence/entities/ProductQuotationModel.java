package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_PRODUCT_QUOTATION, schema = DataBase.Constant.SCH_QUOTATION)
public class ProductQuotationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "quality")
    private String quality;

    @Column(name = "dispatch_date")
    private Timestamp dispatchDate;

    @Column(name = "guide")
    private String guide;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reference")
    private String reference;

    @Column(name = "description")
    private String description;

    @Column(name = "gross_price")
    private Double grossPrice;

    @Column(name = "price")
    private Double netPrice;

    @Column(name = "accept_date")
    private Timestamp acceptDate;

    @Column(name = "importer")
    private Boolean importer;

    @Column(name = "tiempo_entrega")
    private Integer deliveryTime;

    @Column(name = "purchase")
    private Boolean purchase;

    @Column(name = "suggested_reference", columnDefinition = "TEXT")
    private String suggestedReference;

    @Column(name = "value_extra_cost")
    private Double valueExtraCost;

    @Column(name = "extra_cost")
    private Boolean extraCost;

    @Column(name = "max_delivery_days")
    private Boolean maxDeliveryDays;

    @Column(name = "value_max_delivery_days")
    private Integer valueMaxDeliveryDays;

    @Column(name = "discount_additional")
    private Double discountAdditional;

    @Column(name = "discount_brand")
    private Double discountBrand;

    @Column(name = "discount_campaigns")
    private Double discountCampaigns;

    @Column(name = "auth")
    private boolean auth;

    @Column(name = "position_piece")
    private int position;

    @Column(name = "promise_delivery")
    private Timestamp promiseDelivery;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "discount_manual")
    private Double discountManual;

    @Column(name = "manual_purchase_id")
    private Long manualPurchaseId;

    @Column(name = "winner")
    private Boolean winner;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "max_cost_piece")
    private Boolean maxCostPiece;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "purchase_subsidiary", columnDefinition = "boolean default false")
    private boolean purchaseSubsidiary;

    @Column(name = "send_valoration_quotation")
    private Boolean sendValuationQuotation;

    @Column(name = "send_valoration_purchase")
    private Boolean sendValuationPurchase;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    @JsonIgnore
    private QuotationModel quotation;

}
