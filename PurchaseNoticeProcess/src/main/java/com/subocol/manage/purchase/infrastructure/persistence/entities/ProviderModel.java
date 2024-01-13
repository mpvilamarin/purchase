package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_PROVIDER, schema = DataBase.Constant.SCH_PROVIDER)
public class ProviderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nit", nullable = false, unique = true)
    private String nit;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false, name = DataBase.Constant.TBL_CONTACT_NAME)
    @JsonProperty("contact_name")
    private String contactName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "location_external_id", nullable = false)
    @JsonIgnore
    private Long locationExternalId;

    @Column(name = "type_provider", nullable = false)
    private char typeProvider;

    @Column(name = "negotiated_discount", nullable = true)
    private Float negotiatedDiscount;

    @Column(name = "provider_classification", nullable = true)
    private String providerClassification;

    @Column(name = "intermediation_discount", nullable = true)
    private Double intermediationDiscount;

    @Column(name = "contact_id", nullable = true)
    private Integer contactId;

    @Column(name = "devolutiontime")
    private Long devolutionTime;

    @Column(name = "authorized_network")
    private Boolean authorizedNetwork;

    @Column(name = "minimum_amount")
    private Double minimumAmount;

    @Column(name = "invoice_subsidiary")
    private Boolean invoiceSubsidiary;

    @Column(name = "email_accounting")
    private String emailAccounting;

    @Column(name = "email_electronic_invoice")
    private String emailElectronicInvoice;

    @Column(name = "days_intermediation_mostrador")
    private Integer daysIntermediationCounter;

//        // virtual attribute
//    @Transient
//    private LocationDTO location;
//
//    // virtual attribute
//    @JsonProperty("admin_user")
//    @Transient
//    private AdminUserDTO adminUser;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerModel")
//    @JsonIgnore
//    private Set<DiscountBrandModel> discountBrandModel;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerModel")
//    @JsonIgnore
//    private Set<DiscountCampaignsModel> discountCampaignModels;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerModel")
//    @JsonIgnore
//    private Set<DiscountAdditionalModel> discountAdditionalModel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    @JsonIgnore
    private Set<ProviderDMS> providerDMS;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    @JsonIgnore
    private Set<SubsidiaryModel> subsidiaries;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProviderGroupModel> groups;
}