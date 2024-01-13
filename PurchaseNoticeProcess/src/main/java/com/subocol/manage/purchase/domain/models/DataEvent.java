package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class DataEvent {

    private Long id;

    private Integer externalEvent;

    private String workshopCity;

    private String workshopName;

    private String line;

    private String brand;

    private String coverage;

    private Double deductible;

    private String repairConcl;

    private Double fixedDeductible;

    private Double insuredValue;

    private String vin;

    private String plate;

    private String idJob;

    private String unexpected;

    private Long countryId;

    private String model;

    private String version;

    private String workshopRut;

    private String workshopAddress;

    private Long workshopPhone;

    private String claimNumber;

    private String insuranceNumber;

    private String authorization;

    private String workshopType;

    private String workshopNit;

    private String vehicleType;

    private Long noticeSofia;

    private Timestamp date;

    private Long repairOrder;

    private Double lossIndicator;

    private Double totalWorkforce;

    private Integer workshopId;

}

