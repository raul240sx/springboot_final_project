package com.store.sales_api.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
    String code,
    String name,
    String brand,
    BigDecimal price,
    Integer stock
) {

}
