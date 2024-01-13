package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Quotation {

    private Long id;

    private String providerName;

    private String nit;

    private String quotationSubsidiaryName;

    private String replacementReference;

    private Integer unities;

    private Double price;

    private String quality;

    private Boolean importation;

    private Integer timeDelivery;

    private String observations;

    private Subsidiary subsidiary;
    //private Long subsidiaryId;

    private String brand;

    private String line;

    private String city;

    private String status;

    private String externalEvent;

    private Timestamp time;

    private String flowType;

    private Long noticeId;

    private Boolean unforeseen;

    private BigDecimal repairOrder;

    private List<ProductQuotation> productQuotations = new ArrayList<>();

    //private Set<ProductOverruncost> productOverruncost= new HashSet<>();
    //private Set<Order> order= new HashSet<>();

    private boolean adiUpdated;

    private Timestamp dateUpdateQuotation;

    private boolean quotationManaged;

    private boolean quotationWinner;
}