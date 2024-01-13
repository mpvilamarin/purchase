package com.subocol.manage.purchase.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendReserveManage {

    private Long id;

    private Integer externalEvent;

    private Boolean initCarSended;

    private Timestamp date;
}
