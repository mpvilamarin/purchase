package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoticeValuationDTO {

    private int numeroAviso;
    private List<PiecesValuationDTO> piezas = new ArrayList<>();
}
