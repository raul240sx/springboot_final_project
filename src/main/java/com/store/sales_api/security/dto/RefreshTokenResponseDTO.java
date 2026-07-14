package com.store.sales_api.security.dto;

import java.time.Instant;


public record RefreshTokenResponseDTO(
    String rawToken,
    String vendorCode,
    Instant expiryDate
) {

}
