package com.store.sales_api.security.dto;

import org.springframework.http.ResponseCookie;

public record CookiesResponseDTO(
    ResponseCookie accessCookie,
    ResponseCookie refreshCookie
) {

}
