package com.store.sales_api.security.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    
    @NotBlank(message = "{login.username.blank}")
    String username,

    @NotBlank(message = "{login.password.blank}")
    String password) {
}
