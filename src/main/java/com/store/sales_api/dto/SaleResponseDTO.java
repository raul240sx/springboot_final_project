package com.store.sales_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaleResponseDTO(
    Long id,
    String code,
    LocalDate date,
    BigDecimal totalAmount,
    Long client_id,
    List<DetailResponseDTO> details
) {

}
