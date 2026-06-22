package com.store.sales_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;


public record DetailRequestDTO(
    
    @NotNull(message = "{detail.product.id.null}")
    @PositiveOrZero(message = "{detail.product.id.positive.zero}")
    Long productId,

    @NotNull(message = "{detail.quantity.null}")
    @Positive(message = "{detail.quantity.positive.zero}")
    Integer quantity
) {

}
