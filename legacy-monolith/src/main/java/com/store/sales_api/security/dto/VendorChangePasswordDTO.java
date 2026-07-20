package com.store.sales_api.security.dto;

import jakarta.validation.constraints.NotBlank;


public record VendorChangePasswordDTO(

    @NotBlank(message = "vendor.password.null")
    String password,

    @NotBlank(message = "vendor.confirmPassword.null")
    String confirmPassword
) {

}
