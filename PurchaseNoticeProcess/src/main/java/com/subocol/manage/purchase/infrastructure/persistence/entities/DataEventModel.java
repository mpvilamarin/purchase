package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_DATA_EVENT, schema = DataBase.Constant.SCH_SBC_EVENT)
public class DataEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event", nullable = false)
    private Integer externalEvent;

    @Column(name = "workshop_city")
    private String workshopCity;

    @Column(name = "workshop_name")
    private String workshopName;

    @Column(name = "line", nullable = false)
    private String line;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "coverage", nullable = false)
    private String coverage;

    @Column(name = "deductible")
    private double deductible;

    @Column(name = "repair_concl")
    private String repairConcl;

    @Column(name = "fixed_deductible")
    private double fixedDeductible;

    @Column(name = "insured_value")
    private double insuredValue;

    @Column(name = "vin")
    private String vin;

    @Column(name = "license_plate")
    private String plate;

    @Column(name = "id_job")
    private String idJob;

    @Column(name = "unexpected")
    private String unexpected;

    @Column(name = "id_country")
    private Long countryId;

    @Column(name = "model")
    private String model;

    @Column(name = "version")
    private String version;

    @Column(name = "workshop_rut", length = 40)
    private String workshopRut;

    @Column(name = "workshop_address")
    private String workshopAddress;

    @Column(name = "workshop_phone")
    private BigDecimal workshopPhone;

    @Column(name = "claim_number", length = 50)
    private String claimNumber;

    @Column(name = "insurance_number", length = 40)
    private String insuranceNumber;

    @Column(name = "authorizacion", columnDefinition = "TEXT")
    private String authorization;

    @Column(name = "workshop_type", length = 40)
    private String workshopType;

    @Column(name = "workshop_nit", length = 20)
    private String workshopNit;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "sofia_event")
    private Long noticeSofia;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "repair_order")
    private BigDecimal repairOrder;

    @Column(name = "loss_indicator")
    private Double lossIndicator;

    @Column(name = "total_workforce")
    private Double totalWorkforce;

    @Column(name = "id_taller")
    private Integer workshopId;

}

