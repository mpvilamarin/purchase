package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

import static com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase.Constant.TBL_DELETED_PIECES_HISTORY;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(schema = DataBase.Constant.SCH_SBC_EVENT, name = TBL_DELETED_PIECES_HISTORY)
public class DeletedPiecesHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_event")
    private String externalEvent;

    @Column(name = "position_piece")
    private Integer positionPiece;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "deleted_cause")
    private String deletedCause;

    @Column(name = "deleted_date")
    private Timestamp deletedDate;

}