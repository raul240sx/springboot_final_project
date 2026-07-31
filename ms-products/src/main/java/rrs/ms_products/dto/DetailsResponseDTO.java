package rrs.ms_products.dto;

import java.math.BigDecimal;

public record DetailsResponseDTO(
    String productCode,
    Integer quantity,
    BigDecimal partialAmount
) {

}
