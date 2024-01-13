package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PiecesValuationDTO {

    private int posicion;
    private String codigo;
    private String referencia;
    private int cantidad;
    private Double valorUnitario;
    private Double valorUnitarioConDescuento;
    private String comprada;
    private int tiempoEstimadoEntrega;
    private String calidadRepuesto;
    private Double descuento;
    private String nombreSucursalGanadora;

    @JsonIgnore
    private Long id;

    public PiecesValuationDTO(Long id, int posicion, String codigo, String referencia, int cantidad, Double valorUnitario, Double valorUnitarioConDescuento, String status, int tiempoEstimadoEntrega, String calidadRepuesto, Double descuento, String nombreSucursalGanadora) {
        this.id = id;
        this.posicion = posicion;
        this.codigo = codigo;
        this.referencia = referencia;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.valorUnitarioConDescuento = valorUnitarioConDescuento;
        if(status.contentEquals(ManageNoticeConstant.ACCEPTED) || status.contentEquals(ManageNoticeConstant.BOUGHT)) {
            this.comprada = "S";
        }else {
            this.comprada = "N";
        }
        this.tiempoEstimadoEntrega = tiempoEstimadoEntrega;
        this.calidadRepuesto = calidadRepuesto;
        this.descuento = descuento;
        this.nombreSucursalGanadora = nombreSucursalGanadora;
    }
}
