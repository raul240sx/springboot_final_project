package rrs.ms_auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rrs.ms_auth.common.DTOMapper;
import rrs.ms_auth.common.exception.BusinessRuleException;
import rrs.ms_auth.common.exception.ResourceNotFoundException;
import rrs.ms_auth.common.util.TokenEncrypt;
import rrs.ms_auth.dto.RefreshTokenResponseDTO;
import rrs.ms_auth.model.RefreshToken;
import rrs.ms_auth.model.Vendor;
import rrs.ms_auth.repository.IRefreshTokenRepository;
import rrs.ms_auth.repository.IVendorRepository;




@Service
public class RefreshTokenService implements IRefreshTokenService{
    private final IRefreshTokenRepository refreshTokenRepository;
    private final IVendorRepository vendorRepository;
    private final TokenEncrypt tokenEncrypt;
    private final DTOMapper dtoMapper;

    @Value("${app.security.jwt.refresh-token-expiration-seconds}")
    private Long refreshExpiration;


    public RefreshTokenService(IRefreshTokenRepository refreshTokenRepository, IVendorRepository vendorRepository, TokenEncrypt tokenEncrypt, DTOMapper dtoMapper) {
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
        Instant refreshExpiricyDate = Instant.now().plus(refreshExpiration, ChronoUnit.SECONDS);
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
