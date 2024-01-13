package com.subocol.manage.purchase.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ManageNoticeCCDTO {

    private Long noticeId;
    private Long quotationId;
    private List<Long> listProductQuotationIds;
    private Boolean serviceIntegration;
}