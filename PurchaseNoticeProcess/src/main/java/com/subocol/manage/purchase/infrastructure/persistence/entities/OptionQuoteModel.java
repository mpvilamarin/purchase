package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_OPTION_QUOTE, schema = DataBase.Constant.SCH_ADI_API)
public class OptionQuoteModel {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "piece")
    private String piece;

    @Column(name = "irs")
    private String irs;

    @Column(name = "price")
    private Double price;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    @Column(name = "grouping")
    private Integer grouping;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "subsidiary_id")
    private Long subsidiaryId;

    @Column(name = "event")
    private String event;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "breach")
    private Double breach;

    @Column(name = "competitiveness")
    private Double competitiveness;

    @Column(name = "position_piece")
    private Integer positionPiece;

    @Column(name = "notice_id")
    private Long noticeId;

}
