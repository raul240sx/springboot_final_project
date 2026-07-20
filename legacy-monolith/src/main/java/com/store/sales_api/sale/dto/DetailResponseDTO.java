package com.store.sales_api.sale.dto;

import java.math.BigDecimal;


public record DetailResponseDTO(
    String productCode,
    Integer quantity,
    BigDecimal partialAmount
) {

}
