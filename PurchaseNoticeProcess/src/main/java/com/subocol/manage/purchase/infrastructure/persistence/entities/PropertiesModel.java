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
@Table(schema = DataBase.Constant.SCH_PARAMETERS, name = DataBase.Constant.TBL_PROPERTIES)
public class PropertiesModel {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "expose_by")
    private String exposeBy;

    @Column(name = "consume_by")
    private String consumeBy;

    @Column(name = "property")
    private String property;

    @Column(name = "observations")
    private String observations;

}
