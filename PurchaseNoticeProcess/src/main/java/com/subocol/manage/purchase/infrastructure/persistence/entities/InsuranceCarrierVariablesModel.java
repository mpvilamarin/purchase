package com.subocol.manage.purchase.infrastructure.persistence.entities;

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
@Table(name = DataBase.Constant.TBL_INSURANCE_CARRIER_VARIABLES, schema = DataBase.Constant.SCH_PARAMETERS)
public class InsuranceCarrierVariablesModel {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "insurance_id")
    private Long insuranceId;

    @Column(name = "time_minute_quote", columnDefinition = "SMALLINT")
    private int timeMinuteQuote;

    @Column(name = "time_hour_quote", columnDefinition = "SMALLINT")
    private int timeHourQuote;

    @Column(name = "start_work_hour", columnDefinition = "SMALLINT")
    private int startWorkHour;

    @Column(name = "end_work_hour", columnDefinition = "SMALLINT")
    private int endWorkHour;

    @Column(name = "max_delivery_days", columnDefinition = "SMALLINT")
    private int maxDeliveryDays;

    @Column(name = "extra_cost")
    private Double extraCost;

    @Column(name = "antiquity_quote_light")
    private int antiquityQuoteLight;

    @Column(name = "time_minute_service_adi")
    private int timeMinuteServiceAdi;

    @Column(name = "next_call_service_adi")
    private Timestamp nextCallServiceAdi;

    @Column(name = "max_cost_piece_cc")
    private Double maxCostPieceCC;

    @Column(name = "max_cost_piece_mm")
    private Double maxCostPieceMM;
}
