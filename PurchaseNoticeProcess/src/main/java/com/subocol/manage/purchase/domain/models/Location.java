package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@Builder
@ToString
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private Long id;
    private String address;
    private Long countryId;
    private String cityName;
}
