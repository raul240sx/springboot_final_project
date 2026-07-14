package com.store.sales_api.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.common.exception.BusinessRuleException;
import com.store.sales_api.common.exception.ResourceNotFoundException;
import com.store.sales_api.common.util.TokenEncrypt;
import com.store.sales_api.security.IRefreshTokenRepository;
import com.store.sales_api.security.IVendorRepository;
import com.store.sales_api.security.RefreshToken;
import com.store.sales_api.security.SecurityDTOMapper;
import com.store.sales_api.security.Vendor;
import com.store.sales_api.security.dto.RefreshTokenResponseDTO;


@Service
public class RefreshTokenService implements IRefreshTokenService{
    private final IRefreshTokenRepository refreshTokenRepository;
    private final IVendorRepository vendorRepository;
    private final TokenEncrypt tokenEncrypt;
    private final SecurityDTOMapper dtoMapper;

    @Value("${app.security.jwt.refresh-expiration-days}")
    private Long refreshExpirationDays;


    public RefreshTokenService(IRefreshTokenRepository refreshTokenRepository, IVendorRepository vendorRepository, TokenEncrypt tokenEncrypt, SecurityDTOMapper dtoMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.vendorRepository = vendorRepository;
        this.tokenEncrypt = tokenEncrypt;
        this.dtoMapper = dtoMapper;
    }



    @Override
    @Transactional
    public RefreshTokenResponseDTO createRefreshToken(String vendorCode) {
        Vendor vendor = vendorRepository.findByCode(vendorCode).orElseThrow(() -> new ResourceNotFoundException("El vendedor seleccionado no ha sido encontrado en los registros"));
        String rawToken = UUID.randomUUID().toString();
        Instant refreshExpiricyDate = Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS);
        String encryptedToken = tokenEncrypt.hashToken(rawToken);

        RefreshToken createdRefreshToken = refreshTokenRepository.save(new RefreshToken(encryptedToken, vendor, refreshExpiricyDate));

        return dtoMapper.refreshTokenToDTO(createdRefreshToken, rawToken);

    }

    @Override
    public RefreshToken findByToken(String refreshToken) {
        String encryptedToken = tokenEncrypt.hashToken(refreshToken);
        return refreshTokenRepository.findByToken(encryptedToken).orElseThrow(() -> new ResourceNotFoundException("El refresh token es inválido"));
    }

    @Override
    @Transactional
    public void verifyExpiration(RefreshToken refreshToken) {
        Instant expireDate = refreshToken.getExpiryDate();

        if (expireDate.isBefore(Instant.now())) {
            throw new BusinessRuleException("Refresh token expirado");
        }
        refreshTokenRepository.delete(refreshToken);
    }


    @Override
    @Transactional
    public void deleteToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
