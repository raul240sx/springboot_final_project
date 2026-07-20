package com.store.sales_api.security.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(
    @NotBlank(message = "{role.name.null}")
    String name
) {

}
