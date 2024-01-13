package com.subocol.manage.purchase.domain.models;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InsuranceCarrier {

    private Long id;

    private String name;

    private String countryId;

    private String nit;

    private String taxAbbreviation;

    private String logo;

    private String prefix;

    private String ivaItbms;

}
