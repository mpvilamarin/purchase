package com.subocol.manage.purchase.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Piece {

    private int id;

    private int position;

    private String code;

    private String description;

    private String type;

    private int quantity;

    private String quality;

    private boolean reQuote;

    private String groupName;

    private Double unitValue;

    private String observations;

}
