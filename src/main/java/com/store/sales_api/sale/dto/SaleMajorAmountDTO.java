package com.store.sales_api.sale.dto;

import java.math.BigDecimal;

public record SaleMajorAmountDTO(
    String saleCode,
    BigDecimal totalAmount,
    Integer productQuantity,
    String clientName,
    String clientLastName,
    String clientCode
) {

}
