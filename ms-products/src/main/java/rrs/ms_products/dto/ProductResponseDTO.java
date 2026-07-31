package rrs.ms_products.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
    String code,
    String name,
    String brand,
    String category,
    BigDecimal price,
    Integer stock
) {

}
