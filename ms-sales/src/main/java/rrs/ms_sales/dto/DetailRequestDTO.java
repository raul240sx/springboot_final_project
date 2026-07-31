package rrs.ms_sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record DetailRequestDTO(
    
    @NotBlank(message = "{detail.product.code.null}")
    String productCode,

    @NotNull(message = "{detail.quantity.null}")
    @Positive(message = "{detail.quantity.positive.zero}")
    Integer quantity
) {

}
