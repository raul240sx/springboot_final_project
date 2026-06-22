package com.store.sales_api.dto;

public record ClientResponseDTO(
    Long id,
    String name,
    String lastName,
    String dni
) {

}
