package rrs.ms_products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetailsRequestDTO(

    @NotBlank(message = "{detail.product.code.null}")
    String productCode,

    @NotNull(message = "{detail.product.quantity.null}")
    @Positive(message = "{detail.product.quantity.positive.zero}")
    Integer quantity
) {

}
