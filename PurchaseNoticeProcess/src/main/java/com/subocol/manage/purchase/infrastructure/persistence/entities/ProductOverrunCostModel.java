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
@Table(name = DataBase.Constant.TBL_PRODUCT_OVERRUN_COST, schema = DataBase.Constant.TBL_PROVIDER)
public class ProductOverrunCostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_event")
    private String externalEvent;

    @Column(name = "brand")
    private String brand;

    @Column(name = "line")
    private String line;

    @Column(name = "plate")
    private String plate;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "reference")
    private String reference;

    @Column(name = "suggested_reference")
    private String suggestedReference;

    @Column(name = "tiempo_entrega")
    private Integer timeDelivery;

    @Column(name = "importer")
    private boolean importer;

    @Column(name = "value_extra_cost")
    private Double valueExtraCost;

    @Column(name = "extra_cost")
    private Boolean extraCost;

    @Column(name = "net_price")
    private Double netPrice;

    @Column(name = "gross_price")
    private Double grossPrice;

    @Column(name = "discount_additional")
    private Double discountAdditional;

    @Column(name = "discount_brand")
    private Double discountBrand;

    @Column(name = "discount_campaigns")
    private Double discountCampaigns;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "max_delivery_days")
    private Boolean maxDeliveryDays;

    @Column(name = "quality")
    private String quality;

    @Column(name = "comment")
    private String comment;

    @Column(name = "piece_id")
    private Long pieceId;

    @Column(name = "discount_manual")
    private Double discountManual;

    @Column(name = "position")
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    @JsonIgnore
    private QuotationModel quotation;

}
