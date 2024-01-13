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
@Table(schema = DataBase.Constant.SCH_PARAMETERS, name = DataBase.Constant.TBL_INTEGRATION_AWS)
public class IntegrationAWSModel {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "secret_access_key")
    private String secretAccessKey;

}
