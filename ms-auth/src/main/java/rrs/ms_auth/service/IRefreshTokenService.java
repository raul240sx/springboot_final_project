package rrs.ms_auth.service;

import rrs.ms_auth.dto.RefreshTokenResponseDTO;
import rrs.ms_auth.model.RefreshToken;


public interface IRefreshTokenService {

    RefreshTokenResponseDTO createRefreshToken(String vendorCode);

    RefreshToken findByToken(String refreshToken);

    void verifyExpiration(RefreshToken refreshToken);

    void deleteToken(RefreshToken refreshToken);
}
