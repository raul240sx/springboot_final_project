package com.store.sales_api.dto;

import java.math.BigDecimal;


public record DetailResponseDTO(
    Long saleId,
    Long productId,
    Integer quantity,
    BigDecimal partialAmount
) {

}
