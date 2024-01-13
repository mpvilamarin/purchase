package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class NoticeClaimNumberDTO {

    private Integer externalEvent;

    private Long countPackages;

    private Long countClaimEquals;

    public NoticeClaimNumberDTO(Integer externalEvent, Long countPackages, Long countClaimEquals) {
        this.externalEvent = externalEvent;
        this.countPackages = countPackages;
        this.countClaimEquals = countClaimEquals;
    }

}
