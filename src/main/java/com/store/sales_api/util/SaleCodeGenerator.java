package com.store.sales_api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;


@Component
public class SaleCodeGenerator {

    public String generateSaleCode(LocalDate date, Long saleCount) {
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String stringDate = date.format(dateFormatter);
        String saleNumber = String.valueOf(saleCount) + "0".repeat(4 - String.valueOf(saleCount).length());

        return stringDate + saleNumber;
    }
}
