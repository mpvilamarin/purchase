package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Notice
{
	private Long id;

	private Integer externalEvent;

	private String plate;

	private Timestamp date;

	private String brand;

	private String line;

    private String model;

    private String version;

	private String workshop;

	private String city;

	private String status;

	private String vin;

	private Set<Order> orders = new HashSet<>();

	private String workshopEmail;

	private BigDecimal phone;

	private String cellphone;

	private String coverage;

	private String workshopAddress;

	private Long idCountry;

	private boolean auth;

    private Long insuranceNumber;

    private String workshopType;

    private String statusNotice;

    private Timestamp dateClose;

    private String claimNumber;

    private Long eventId;

	private boolean unforeseen;

	private BigDecimal repairOrder;  

	private Timestamp quotationEstimatedDate; 

	private boolean closed;

	private Double lossIndicator;

	private Double totalWorkforce;

    private Double insuredValue;

	private boolean processedNotice;

	private boolean consultedReferenceIA;

	private Timestamp nextDateReferenceIa;

    private Integer workshopId;

}
