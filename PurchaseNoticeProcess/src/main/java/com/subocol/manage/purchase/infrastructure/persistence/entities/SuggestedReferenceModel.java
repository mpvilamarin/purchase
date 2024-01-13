package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_SUGGESTED_REFERENCE, schema = DataBase.Constant.SCH_SBC_EVENT)
public class SuggestedReferenceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reference", columnDefinition = "TEXT")
    private String reference;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "pieces_id")
    private PieceModel piece;

}
