package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_DESIST, schema = DataBase.Constant.SCH_PROVIDER)
public class DesistModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "causal", length= 50)
    private String causal;

    @Column(name = "observation", length= 100)
    private String observation;

    @Column(name = "id_product_order")
    private Long idProductOrder;

    @Column(name = "id_order")
    private	Long idOrder;
}
