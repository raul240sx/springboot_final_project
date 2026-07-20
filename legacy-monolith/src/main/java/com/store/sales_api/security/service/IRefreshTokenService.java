package com.store.sales_api.security.service;

import com.store.sales_api.security.RefreshToken;
import com.store.sales_api.security.dto.RefreshTokenResponseDTO;


public interface IRefreshTokenService {

    RefreshTokenResponseDTO createRefreshToken(String vendorCode);

    RefreshToken findByToken(String refreshToken);

    void verifyExpiration(RefreshToken refreshToken);

    void deleteToken(RefreshToken refreshToken);
}
