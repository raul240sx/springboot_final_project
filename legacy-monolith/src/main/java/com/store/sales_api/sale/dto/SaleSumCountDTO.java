package com.store.sales_api.sale.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleSumCountDTO(
    LocalDate date,
    Integer saleCount,
    BigDecimal dayTotalAmount
) {

}
