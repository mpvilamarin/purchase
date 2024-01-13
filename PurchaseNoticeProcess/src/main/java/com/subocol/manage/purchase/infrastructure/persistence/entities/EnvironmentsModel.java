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
@Table(name = DataBase.Constant.TBL_ENVIRONMENTS, schema = DataBase.Constant.SCH_PARAMETERS)
public class EnvironmentsModel {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "environments")
    private String environments;

}
