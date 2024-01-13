package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class PiecesQuoteAuthDTO {

    @JsonProperty("posicion")
    private int position;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("referencia")
    private String reference;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("calidadRepuesto")
    private String sparePartQuality;

    @JsonProperty("cantidad")
    private int quantity;

    @JsonProperty("valorUnitario")
    private Double unitValue;

    @JsonProperty("foundIt")
    private boolean foundIt;

}
