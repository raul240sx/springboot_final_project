package com.store.sales_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO(
    @NotBlank(message = "{client.name.null}")
    String name,
    @NotBlank(message = "{client.last.name.null}")
    String lastName,
    @NotBlank(message = "{client.dni.null}")
    String dni
) {

}
