package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_MANUAL_PURCHASE_ADI, schema = DataBase.Constant.SCH_ADI_API)
public class ManualPurchaseAdiModel {

    @Id
    @Column(name = "id_register")
    private Long idRegister;

    @Column(name = "irs")
    private String irs;

    @Column(name = "pieces")
    private String pieces;

    @Column(name = "brand")
    private String brand;

    @Column(name = "line")
    private String line;

    @Column(name = "version")
    private String version;

    @Column(name = "vin")
    private String vin;

    @Column(name = "event")
    private Long externalEvent;

    @Column(name = "quantity", columnDefinition = "NUMERIC")
    private int quantity;

    @Column(name = "quality")
    private String quality;

    @Column(name = "homologated_name", columnDefinition = "TEXT")
    private String homologatedName;

    @Column(name = "group_name", columnDefinition = "TEXT")
    private String group;

    @Column(name = "subgroup", columnDefinition = "TEXT")
    private String subgroup;

    @Column(name = "suggested_reference", columnDefinition = "TEXT")
    private String suggestedReference;

    @Column(name = "position_piece")
    private int position;

    @Column(name = "type", columnDefinition = "TEXT")
    private String type;

    @Column(name = "manual_purchases")
    private boolean manualPurchase;

    @Column(name = "event_id")
    private Long eventId;

}
