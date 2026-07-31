package rrs.ms_sales.dto;

import java.math.BigDecimal;


public record DetailResponseDTO(
    String productCode,
    Integer quantity,
    BigDecimal partialAmount
) {

}
