package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DataBase.Constant.TBL_CURRENCIES, schema = DataBase.Constant.SCH_TAXES)
public class CurrencyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_id_dms")
    private Integer currencyId;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "description")
    private String description;

    @Column(name = "divide")
    private Integer divide;

    @Column(name = "fixed_rate")
    private Double fixedRate;

    @Column(name = "country_id")
    private Long countryId;

}