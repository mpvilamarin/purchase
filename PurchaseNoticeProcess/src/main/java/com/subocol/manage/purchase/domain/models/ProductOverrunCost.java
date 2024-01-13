package com.subocol.manage.purchase.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subocol.manage.purchase.infrastructure.persistence.entities.QuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ProductOverrunCost {


    private Long id;

    private String externalEvent;

    private String brand;

    private String line;

    private String plate;

    private String description;

    private Integer quantity;

    private String reference;

    private String suggestedReference;

    private Integer timeDelivery;

    private boolean importer;

    private Double valueExtraCost;

    private Boolean extraCost;

    private Double netPrice;

    private Double grossPrice;

    private Double discountAdditional;

    private Double discountBrand;

    private Double discountCampaigns;

    private String status;

    private Timestamp date;

    private Boolean maxDeliveryDays;

    private String quality;

    private String comment;

    private Long pieceId;

    private Double discountManual;

    private Integer position;

    private Quotation quotation;

}
