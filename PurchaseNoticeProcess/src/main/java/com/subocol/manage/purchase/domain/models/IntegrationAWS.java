package com.subocol.manage.purchase.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class IntegrationAWS {

    private Long id;

    private String name;

    private String accessKey;

    private String secretAccessKey;

}