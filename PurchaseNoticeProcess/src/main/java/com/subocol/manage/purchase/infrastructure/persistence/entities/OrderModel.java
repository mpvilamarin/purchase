package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_ORDERS, schema = DataBase.Constant.SCH_PROVIDER)
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "status")
    private String status;

    @Column(name = "workshop")
    private String workshop;

    @Column(name = "time")
    private Integer time;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "reference")
    private String reference;

    @Column(name = "comment")
    private String comment;

    @Column(name = "fecha_entrega_taller")
    private Timestamp workshopDeliveryDate;

    @Column(name = "url_documento")
    private String documentUrl;

    @Column(name = "order_id_dms")
    private Integer orderIdDms;

    @Column(name = "order_purchase_dms")
    private Integer orderPurchaseDms;

    @Column(name = "order_purchase_chile")
    private Long orderPurchaseChile;

    @Column(name = "order_id_subocol")
    private String orderIdSubocol;

    @Column(name = "billing_service_id")
    private Integer billingServiceId;

    @Column(name = "unforeseen")
    private Boolean unforeseen;

    @Column(name = "repair_order")
    private BigDecimal repairOrder;

    @Column(name = "purchase_type_id")
    private Integer purchaseTypeId;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private NoticeModel notice;

    @ManyToOne
    @JoinColumn(name = "subsidiary_id")
    private SubsidiaryModel subsidiary;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
    private Set<ProductOrderModel> products;

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    private SellOrderModel sellOrder;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    @JsonIgnore
    private QuotationModel quotation;
}