package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Immutable
@Table(name = DataBase.Constant.TBL_PROVIDER_SUBGROUP, schema = DataBase.Constant.SCH_PROVIDER)
public class ProviderSubgroupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_subgroup", nullable = false)
    private String nameSubgroup;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_user", nullable = false)
    private String creationUser;

    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @ManyToOne
    @JoinColumn(name = "provider_group_id")
    private ProviderGroupModel providerGroup;

}