package com.store.sales_api.security.service;

import com.store.sales_api.security.dto.CookiesResponseDTO;
import com.store.sales_api.security.dto.LoginRequestDTO;


public interface IAuthService {

    CookiesResponseDTO login(LoginRequestDTO dto);
    
    CookiesResponseDTO refresh(String refreshToken);

    CookiesResponseDTO logout(String refreshToken);
}
