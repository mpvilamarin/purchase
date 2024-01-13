package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_SUBSIDIARY, schema = DataBase.Constant.SCH_PROVIDER)
public class SubsidiaryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(nullable = false, name = "location_external_id")
    @JsonIgnore
    private Long locationExternalId;

    @Column(name = "warehouse_id_dms")
    private Integer warehouseIdDms;

    @Column(name = "classification")
    private String classification;

    @Column(name = "id_job")
    private String idJob;

    @Column(name = "dms_code")
    private Long dmsCode;

    @Column(name = "intermediacion")
    private Integer intermediation;

//    	// virtual attributes
//	@JsonProperty("time_cities_delivery")
//	@Transient
//	private List<TimeDeliveryCityDTO> timeDeliveryCity;
//
//	@JsonProperty("time_regions_delivery")
//	@Transient
//	private List<TimeDeliveryRegionDTO> timeDeliveryRegion;
//
//	@Transient
//	private LocationDTO location;
//
//	@Transient
//	private LocationDetailDTO locationDetail;
//    @JsonProperty("admin_user")
//    @Transient
//    private AdminUserDTO adminUser;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ProviderModel provider;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subsidiary")
    @JsonIgnore
    private Set<OrderModel> orders;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<QuotationModel> quotations;

}
