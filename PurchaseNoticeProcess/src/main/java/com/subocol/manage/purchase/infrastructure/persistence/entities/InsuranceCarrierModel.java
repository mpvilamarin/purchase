package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = DataBase.Constant.TBL_INSURANCE_CARRIER, schema = DataBase.Constant.SCH_SBC_EVENT)
public class InsuranceCarrierModel {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "nit")
    private String nit;

    @Column(name = "tax_abbreviation", length = 5)
    private String taxAbbreviation;

    @Column(name = "logo")
    private String logo;

    @Column(name = "prefix", length = 3)
    private String prefix;

    @Column(name = "iva_itbms", length = 5)
    private String ivaItbms;

}
