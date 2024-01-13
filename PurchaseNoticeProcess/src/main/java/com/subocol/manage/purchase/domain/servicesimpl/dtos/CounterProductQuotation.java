package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounterProductQuotation {

    private Long position;
    private Boolean auth;
    private Long totalProducts;
    private Long omittedProducts;
    private Long rejectedQuotedProducts;
    private Long alertAndWinnerProducts;
    private Long extraCost;
    private Long overTime;
    private Long maxCostPiece;

}
