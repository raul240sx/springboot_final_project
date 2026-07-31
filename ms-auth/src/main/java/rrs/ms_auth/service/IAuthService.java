package rrs.ms_auth.service;

import rrs.ms_auth.dto.CookiesResponseDTO;
import rrs.ms_auth.dto.LoginRequestDTO;


public interface IAuthService {

    CookiesResponseDTO login(LoginRequestDTO dto);
    
    CookiesResponseDTO refresh(String refreshToken);

    CookiesResponseDTO logout(String refreshToken);
}
