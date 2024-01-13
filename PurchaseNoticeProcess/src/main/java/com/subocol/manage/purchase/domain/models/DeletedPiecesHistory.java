package com.subocol.manage.purchase.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class DeletedPiecesHistory {

    private Long id;

    private String externalEvent;

    private Integer positionPiece;

    private String userName;

    private String deletedCause;

    private Timestamp deletedDate;

}