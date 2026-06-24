package com.store.sales_api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;


@Component
public class SaleCodeGenerator {
    private final Base62Encoder encoder;

    public SaleCodeGenerator(Base62Encoder encoder) {
        this.encoder = encoder;
    }


    public String generateSaleCode(LocalDate date, Long saleId) {

        String encodedId = encoder.generateCode(saleId);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String stringDate = date.format(dateFormatter);
        String encodedIdZero = "0".repeat(Math.max(0, 4 - encodedId.length())) + encodedId;

        return stringDate + encodedIdZero;
    }
}
