package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_QUOTATION, schema = DataBase.Constant.SCH_QUOTATION)
public class QuotationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_prov", nullable = false)
    private String providerName;

    @Column(name = "nit", nullable = false)
    private String nit;

    @Column(name = "nombre_sucur", nullable = false)
    private String quotationSubsidiaryName;

    @Column(name = "ref", nullable = false)
    private String replacementReference;

    @Column(name = "unidades", nullable = false)
    private Integer unities;

    @Column(name = "precio", nullable = false)
    private Double price;

    @Column(name = "calidad", nullable = false)
    private String quality;

    @Column(name = "importacion", nullable = false)
    private Boolean importation;

    @Column(name = "tiempo_entrega", nullable = false)
    private Integer timeDelivery;

    @Column(name = "observaciones", nullable = false)
    private String observations;

    @Column(name = "marca", nullable = false)
    private String brand;

    @Column(name = "linea", nullable = false)
    private String line;

    @Column(name = "ciudad", nullable = false)
    private String city;

    @Column(name = "estado", nullable = false)
    private String status;

    @Column(name = "aviso")
    private String externalEvent;

    @Column(name = "tiempo")
    private Timestamp time;

    @Column(name = "tipo_flujo")
    private String flowType;

    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "unforeseen")
    private Boolean unforeseen;

    @Column(name = "repair_order")
    private BigDecimal repairOrder;

    @Column(name = "adi_updated")
    private boolean adiUpdated;

    @Column(name = "date_update_quotation")
    private Timestamp dateUpdateQuotation;

    @Column(name = "quotation_managed", columnDefinition = "boolean default false")
    private boolean quotationManaged;

    @Column(name = "quotation_winner", columnDefinition = "boolean default false")
    private boolean quotationWinner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "quotation")
    private List<ProductQuotationModel> productQuotations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation")
    @JsonIgnore
    private Set<ProductOverrunCostModel> productOverrunCosts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation")
    @JsonIgnore
    private Set<OrderModel> orders;

    @ManyToOne
    @JoinColumn(name = "subsidiary_id")
    @JsonIgnore
    private SubsidiaryModel subsidiary;
}