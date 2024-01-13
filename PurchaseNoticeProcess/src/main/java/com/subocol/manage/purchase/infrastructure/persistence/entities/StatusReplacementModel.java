package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_STATUS_REPLACEMENT, schema = DataBase.Constant.SCH_TRACING)
public class StatusReplacementModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_event")
    private String externalEvent;

    @Column(name = "seller")
    private String seller;

    @Column(name = "date_order")
    private Timestamp dateOrder;

    @Column(name = "provider")
    private String provider;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "provider_observation")
    private String providerObservation;

    @Column(name = "quantity_parts")
    private Integer quantityParts;

    @Column(name = "subsidiary")
    private String subsidiary;

    @Column(name = "email_subsidiary")
    private String emailSubsidiary;

    @Column(name = "phone_subsidiary")
    private String phoneSubsidiary;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_replacement")
    private Set<StatusPartsModel> statusParts = new HashSet<>();

}
