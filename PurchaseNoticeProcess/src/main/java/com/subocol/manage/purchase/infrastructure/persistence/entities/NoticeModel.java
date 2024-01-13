package com.subocol.manage.purchase.infrastructure.persistence.entities;

import com.subocol.manage.purchase.infrastructure.persistence.entities.constants.DataBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = DataBase.Constant.TBL_NOTICE, schema = DataBase.Constant.SCH_PROVIDER)
public class NoticeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_event")
    private Integer externalEvent;

    @Column(name = "plate", nullable = false)
    private String plate;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "line", nullable = false)
    private String line;

    @Column(name = "model")
    private String model;

    @Column(name = "version_brand")
    private String version;

    @Column(name = "workshop", nullable = false)
    private String workshop;

    @Column(name = "city", nullable = false)
    private String city;

    @Transient
    private String status;

    @Column(name = "vin", nullable = false)
    private String vin;

    @Column(name = "workshop_email")
    private String workshopEmail;

    @Column(name = "phone")
    private BigDecimal phone;

    @Column(name = "cellphone")
    private String cellphone;

    @Column(name = "coverage")
    private String coverage;

    @Column(name = "workshop_address")
    private String workshopAddress;

    @Column(name = "id_country")
    private Long idCountry;

    @Column(name = "auth")
    private boolean auth;

    @Column(name = "insurance_number")
    private Long insuranceNumber;

    @Column(name = "workshop_type", length = 40)
    private String workshopType;

    @Column(name = "status", length = 30)
    private String statusNotice;

    @Column(name = "date_close")
    private Timestamp dateClose;

    @Column(name = "claim_number", length = 50)
    private String claimNumber;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "unforeseen")
    private boolean unforeseen;

    @Column(name = "repair_order")
    private BigDecimal repairOrder;

    @Column(name = "quotation_estimated_date")
    private Timestamp quotationEstimatedDate;

    @Column(name = "closed")
    private boolean closed;

    @Column(name = "loss_indicator")
    private Double lossIndicator;

    @Column(name = "total_workforce")
    private Double totalWorkforce;

    @Column(name = "insured_value")
    private Double insuredValue;

    @Column(name = "processed_notice")
    private boolean processedNotice;

    @Column(name = "was_consulted_reference_ia")
    private boolean consultedReferenceIA;

    @Column(name = "next_date_referenceia")
    private Timestamp nextDateReferenceIa;

    @Column(name = "id_taller")
    private Integer workshopId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notice", fetch = FetchType.EAGER)
    private Set<OrderModel> orders = new HashSet<>();

}
