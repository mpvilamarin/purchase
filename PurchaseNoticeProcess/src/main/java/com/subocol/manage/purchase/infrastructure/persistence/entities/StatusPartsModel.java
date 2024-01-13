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
@Table(name = DataBase.Constant.TBL_STATUS_PARTS, schema = DataBase.Constant.SCH_TRACING)
public class StatusPartsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_part")
    private String namePart;

    @Column(name = "reference")
    private String reference;

    @Column(name = "import_part")
    private Boolean importPart;

    @Column(name = "total_orders_event")
    private Integer totalOrdersEvent;

    @Column(name = "id_order")
    private Long idOrder;

    @Column(name = "id_product_order")
    private Long idProductOrder;

    @Column(name = "total_parts")
    private Integer totalParts;

    @Column(name = "status")
    private String status;

    @Column(name = "approved_order_date")
    private Timestamp approvedOrderDate;

    @Column(name = "estimate_delivery_date")
    private Timestamp estimateDeliveryDate;

    @Column(name = "delivered_date")
    private Timestamp deliveredDate;

    @Column(name = "on_route_date")
    private Timestamp onRouteDate;

    @Column(name = "devolution_date")
    private Timestamp devolutionDate;

    @Column(name = "late_date")
    private Timestamp lateDate;

    @Column(name = "completed_date")
    private Timestamp completedDate;

    @Column(name = "received_workshop_date")
    private Timestamp receivedWorkshopDate;

    @ManyToOne
    @JoinColumn(name = "id_replacement")
    @JsonIgnore
    private StatusReplacementModel statusReplacement;

}
