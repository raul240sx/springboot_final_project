package com.store.sales_api.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
    Long id,
    String code,
    String name,
    String brand,
    BigDecimal price,
    Integer stock
) {

}
