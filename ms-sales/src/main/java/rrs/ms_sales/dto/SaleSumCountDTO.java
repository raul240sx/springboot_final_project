package rrs.ms_sales.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleSumCountDTO(
    LocalDate date,
    Integer saleCount,
    BigDecimal dayTotalAmount
) {

}
