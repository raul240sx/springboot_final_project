package com.store.sales_api.dto;

import java.math.BigDecimal;


public record DetailResponseDTO(
    String productCode,
    Integer quantity,
    BigDecimal partialAmount
) {

}
