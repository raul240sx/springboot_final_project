package com.store.sales_api.sale.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaleResponseDTO(
    String code,
    LocalDate date,
    BigDecimal totalAmount,
    String clientCode,
    List<DetailResponseDTO> details
) {

}
