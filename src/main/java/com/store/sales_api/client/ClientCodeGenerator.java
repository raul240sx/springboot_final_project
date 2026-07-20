package com.store.sales_api.client;

import org.springframework.stereotype.Component;

import com.store.sales_api.common.util.Base62Encoder;

@Component
public class ClientCodeGenerator {
    private final Base62Encoder encoder;

    public ClientCodeGenerator(Base62Encoder encoder) {
        this.encoder = encoder;
    }


    public String generateSaleCode(Long clientId) {
        String encodedId = encoder.generateCode(clientId);

        String encodedIdZero = "0".repeat(Math.max(0, 6 - encodedId.length())) + encodedId;

        return "CLI-" + encodedIdZero;
    }
}