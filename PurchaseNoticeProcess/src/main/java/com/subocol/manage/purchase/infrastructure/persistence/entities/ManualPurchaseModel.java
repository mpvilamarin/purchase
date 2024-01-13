package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@ToString
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_MANUAL_PURCHASE, schema = DataBase.Constant.SCH_PROVIDER)
public class ManualPurchaseModel {

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

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "cause")
    private String cause;

    @Column(name = "position_piece")
    private Integer position;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "auth")
    private boolean auth;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "purchase_subsidiary", columnDefinition = "boolean default false")
    private boolean purchaseSubsidiary;

}

