package rrs.ms_auth.dto;

import org.springframework.http.ResponseCookie;

public record CookiesResponseDTO(
    ResponseCookie accessCookie,
    ResponseCookie refreshCookie
) {

}
