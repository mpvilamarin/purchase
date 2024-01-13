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
public class ProductOrdersPiecesNotice {

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

    private Integer externalEvent;

    private Boolean unforeseen;
}
