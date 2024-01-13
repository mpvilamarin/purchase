package com.subocol.manage.purchase.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class InsuranceCarrierVariables {

    private Long id;

    private Long insuranceId;

    private int timeMinuteQuote;

    private int timeHourQuote;

    private int startWorkHour;

    private int endWorkHour;

    private int maxDeliveryDays;

    private Double extraCost;

    private int antiquityQuoteLight;

    private int timeMinuteServiceAdi;

    private Timestamp nextCallServiceAdi;

    private Double maxCostPieceCC;

    private Double maxCostPieceMM;
}
