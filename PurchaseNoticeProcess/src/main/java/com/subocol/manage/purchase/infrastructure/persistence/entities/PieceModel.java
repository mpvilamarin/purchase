package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Immutable;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Immutable
@Table(name = DataBase.Constant.TBL_PIECES, schema = DataBase.Constant.SCH_SBC_EVENT)
public class PieceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "position_piece", columnDefinition = "TEXT")
    private int position;

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", columnDefinition = "TEXT")
    private String type;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "quality")
    private String quality;

    @Column(name = "requote")
    private boolean reQuote;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "unit_value")
    private Double unitValue;

    @Column(name = "observaciones")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private DataEventModel dataEvent;
}
