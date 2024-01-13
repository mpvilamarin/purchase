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
@Table(name = DataBase.Constant.TBL_PRODUCT_ORDER, schema = DataBase.Constant.SCH_PROVIDER)
public class ProductOrderModel {

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
    @JsonIgnore
    private Timestamp dispatchDate;

    @Column(name = "guide")
    @JsonIgnore
    private String guide;

    @Column(name = "comment")
    @JsonIgnore
    private String comment;

    @Column(name = "reference")
    private String reference;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "gross_price")
    private Double grossPrice;

    @Column(name = "accept_date")
    private Timestamp acceptDate;

    @Column(name = "importer")
    private Boolean importer;

    @Column(name = "total_discount")
    private Double totalDiscount;

    @Column(name = "promise_delivery")
    private Timestamp promiseDelivery;

    @Column(name = "is_delayed")
    private Boolean delayed;

    @Column(name = "desist")
    private Boolean desist;

    @Column(name = "date_desist")
    private Timestamp dateDesist;

    @Column(name = "delayed_date")
    @JsonIgnore
    private Timestamp delayedDate;

    @Column(name = "promised_delivery_days")
    private Integer promisedDeliveryDays;

    @Column(name = "position_piece")
    private Integer positionPiece;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderModel order;

}