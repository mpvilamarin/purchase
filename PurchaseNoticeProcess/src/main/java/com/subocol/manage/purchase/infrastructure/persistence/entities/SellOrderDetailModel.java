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
@Table(name = DataBase.Constant.TBL_SELL_ORDER_DETAIL, schema = DataBase.Constant.SCH_PROVIDER)
public class SellOrderDetailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "total")
    private Double total;

    @Column(name = "gross_price")
    private Double grossPrice;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "promise_delivery")
    private Timestamp promiseDelivery;

    @Column(name = "comment")
    private String comment;

    @Column(name = "position_piece")
    private Integer positionPiece;

    @ManyToOne
    @JoinColumn(name = "sell_order_id")
    @JsonIgnore
    private SellOrderModel sellOrder;

}