package com.subocol.manage.purchase.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder implements Serializable {

    private Long id;

    private Order order;

    private String status;

    private Integer amount;

    private String quality;

    private Timestamp dispatchDate;

    private String guide;

    private String comment;

    private String reference;

    private String description;

    private Double price;

    private Double grossPrice;

    private Timestamp acceptDate;

    private Boolean importer;

    private Double totalDiscount;

    private Timestamp promiseDelivery;

    private Boolean isDelayed;

    private Boolean desist;

    private Timestamp dateDesist;

    private Timestamp delayedDate;

    private Integer promisedDeliveryDays;

    private Integer positionPiece;

    private String userName;

    public ProductOrder(ProductOrder productOrder) {
        this.description = productOrder.getDescription();
        this.reference = productOrder.getReference();
        this.quality = productOrder.getQuality();
        this.amount = productOrder.getAmount();
    }
}