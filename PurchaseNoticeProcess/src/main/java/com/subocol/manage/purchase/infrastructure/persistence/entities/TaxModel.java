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
@Table(name = DataBase.Constant.TBL_TAXES, schema = DataBase.Constant.SCH_TAXES)
public class TaxModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tax_id_dms")
    private Integer taxIdDms;

    @Column(name = "description")
    private String description;

    @Column(name = "percentage")
    private Integer percentage;

    @Column(name = "type")
    private Integer type;

    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "tax_name")
    private String taxName;

}