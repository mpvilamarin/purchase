package com.subocol.manage.purchase.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString
public class DeletePiecesManualPurchaseDTO {

    private Integer positionPiece;

    @JsonProperty("aviso")
    private String externalEvent;

    private String userName;

    private String deletedCause;

}