package com.store.sales_api.security.dto;

import jakarta.validation.constraints.NotBlank;

public record VendorRequestDTO(
    @NotBlank(message = "{vendor.name.null}")
    String name,

    @NotBlank(message = "vendor.last.name.null")
    String lastName,

    @NotBlank(message = "vendor.dni.null")
    String dni,

    @NotBlank(message = "vendor.password.null")
    String password,

    @NotBlank(message = "vendor.confirmPassword.null")
    String confirmPassword
) {

}
