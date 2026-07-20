package com.store.sales_api.security.dto;

import java.util.List;

public record VendorResponseDTO(
    String code,
    String name,
    String lastName,
    String dni,
    List<String> roles
) {

}
