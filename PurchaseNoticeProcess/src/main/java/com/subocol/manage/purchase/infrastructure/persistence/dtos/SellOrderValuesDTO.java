package com.subocol.manage.purchase.infrastructure.persistence.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellOrderValuesDTO {
    private Double subTotal;
    private Double total;

}
