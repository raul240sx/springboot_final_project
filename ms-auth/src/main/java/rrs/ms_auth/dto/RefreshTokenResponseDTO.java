package rrs.ms_auth.dto;

import java.time.Instant;


public record RefreshTokenResponseDTO(
    String rawToken,
    String vendorCode,
    Instant expiryDate
) {

}
