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
@Table(name = DataBase.Constant.TBL_PROVIDER_DMS, schema = DataBase.Constant.TBL_PROVIDER)
public class ProviderDMS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_provider_DMS", nullable = true)
    private Long idProviderDMS;

    @Column(name = "company_DMS", nullable = true)
    private Long companyDMS;

    @Column(name = "id_seller_DMS", nullable = true)
    private Long idSellerDMS;

    @Column(name = "id_user_DMS", nullable = true)
    private Long idUserDMS;

    @Column(name = "contact_id", nullable = true)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    private ProviderModel provider;

}
