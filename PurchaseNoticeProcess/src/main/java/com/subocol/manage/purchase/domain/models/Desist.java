package com.subocol.manage.purchase.domain.models;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Desist {

    private Long id;

    private String causal;

    private String observation;

    private Long idProductOrder;

    private	Long idOrder;
}
