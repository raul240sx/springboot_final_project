package com.store.sales_api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;


@Component
public class SaleCodeGenerator {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateSaleCode(LocalDate date, Long saleId) {
        StringBuilder encodedId = new StringBuilder();
        while (saleId > 0) {
            int remainder = (int) (saleId % 62);
            encodedId.append(BASE62_CHARS.charAt(remainder));
            saleId = saleId / 62;
        }

        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String stringDate = date.format(dateFormatter);
        String encodedIdZero = "0".repeat(Math.max(0, 4 - String.valueOf(encodedId).length())) + encodedId.reverse();

        return stringDate + encodedIdZero;
    }
}
