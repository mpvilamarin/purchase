package com.subocol.manage.purchase.domain.servicesimpl.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class DesistDTO {

    @NotNull(message = "El campo 'ids' no puede ser nulo.")
    @NotEmpty(message = "El campo 'ids' no puede estar vacio.")
    private List<Long> ids;
    @NotBlank(message = "El campo 'causal' no puede ser nulo o estar vacio.")
    private String causal;

    private String observation;
    @NotBlank(message = "El campo 'userName' no puede ser nulo o estar vacio.")
    private String userName;
}
