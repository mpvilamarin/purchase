package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class AuthNoticePiecesDTO {

    @NotBlank(message = "El campo 'idAviso' no puede ser nulo o estar vacio.")
    @JsonProperty("idAviso")
    private String externalEvent;

    @NotBlank(message = "El campo 'noSiniestro' no puede ser nulo o estar vacio.")
    @JsonProperty("noSiniestro")
    private String claimNumber;

    @NotNull(message = "El campo 'repuestos' no puede ser nulo.")
    @NotEmpty(message = "El campo 'repuestos' no puede estar vacio.")
    @JsonProperty("repuestos")
    private List<PiecesQuoteAuthDTO> spareParts;

}
