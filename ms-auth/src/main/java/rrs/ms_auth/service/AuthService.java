package rrs.ms_auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rrs.ms_auth.common.exception.ResourceNotFoundException;
import rrs.ms_auth.dto.CookiesResponseDTO;
import rrs.ms_auth.dto.LoginRequestDTO;
import rrs.ms_auth.dto.RefreshTokenResponseDTO;
import rrs.ms_auth.model.RefreshToken;




@Service
@RefreshScope
public class AuthService implements IAuthService{
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final IRefreshTokenService refreshTokenService;
    private final CustomVendorDetailService detailService;


    @Value("${app.security.cookie.same-site}")
    private String cookieSameSite;

    @Value("${app.security.jwt.refresh-token-expiration-seconds}")
    private Long refreshTokenExpiration;

    @Value("${app.security.jwt.access-token-expiration-seconds}")
    private Long accessTokenExpiration;


    public AuthService(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, IRefreshTokenService refreshTokenService, CustomVendorDetailService detailService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenService = refreshTokenService;
        this.detailService = detailService;
    }


    @Override
    @Transactional
    public CookiesResponseDTO login(LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        Authentication authentication = authenticationManager.authenticate(credentials);

        String roles = String.join(" ", authentication.getAuthorities().stream().map(authority -> authority.getAuthority()).toList());

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                                .issuer("sales-api")
                                .subject(authentication.getName())
                                .issuedAt(Instant.now())
                                .expiresAt(Instant.now().plus(this.accessTokenExpiration, ChronoUnit.SECONDS))
                                .claim("roles", roles)
                                .build();


        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claimSet);
        Jwt token = jwtEncoder.encode(parameters);

        ResponseCookie accessCookie = ResponseCookie.from("AUTH-TOKEN", token.getTokenValue())
                                    .httpOnly(true)
                                    .secure(true)
                                    .sameSite(this.cookieSameSite)
                                    .maxAge(this.accessTokenExpiration)
                                    .path("/")
                                    .build();


        RefreshTokenResponseDTO refreshToken = refreshTokenService.createRefreshToken(authentication.getName());

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH-TOKEN", refreshToken.rawToken())
                                    .httpOnly(true)
                                    .secure(true)
                                    .sameSite(this.cookieSameSite)
                                    .maxAge(this.refreshTokenExpiration)
                                    .path("/api/auth")
                                    .build();

        
        return new CookiesResponseDTO(accessCookie, refreshCookie);
    }


    @Override
    @Transactional
    public CookiesResponseDTO refresh(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.verifyExpiration(token);
        RefreshTokenResponseDTO newRefreshToken = refreshTokenService.createRefreshToken(token.getVendor().getCode());

        UserDetails userDetails = detailService.loadUserByUsername(token.getVendor().getCode());
        String roles = String.join(" ", userDetails.getAuthorities().stream().map(authority -> authority.toString()).toList());

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                                    .issuer("sales-api")
                                    .subject(userDetails.getUsername())
                                    .issuedAt(Instant.now())
                                    .expiresAt(Instant.now().plus(this.accessTokenExpiration, ChronoUnit.SECONDS))
                                    .claim("roles", roles)
                                    .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claimSet);
        Jwt accessToken = jwtEncoder.encode(parameters);

        ResponseCookie accessCookie = ResponseCookie.from("AUTH-TOKEN", accessToken.getTokenValue())
                                            .httpOnly(true)
                                            .secure(true)
                                            .sameSite(this.cookieSameSite)
                                            .maxAge(accessTokenExpiration)
                                            .path("/")
                                            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH-TOKEN", newRefreshToken.rawToken())
                                            .httpOnly(true)
                                            .secure(true)
                                            .sameSite(this.cookieSameSite)
                                            .maxAge(this.refreshTokenExpiration)
                                            .path("/api/auth")
                                            .build();

        return new CookiesResponseDTO(accessCookie, refreshCookie);
    }


    @Override
    @Transactional
    public CookiesResponseDTO logout(String refreshToken) {

        try {
            RefreshToken token = refreshTokenService.findByToken(refreshToken);
            refreshTokenService.deleteToken(token);

        } catch (ResourceNotFoundException e) {
        }
        

        ResponseCookie accessCookie = ResponseCookie.from("AUTH-TOKEN", null)
                                            .httpOnly(true)
                                            .secure(true)
                                            .sameSite(this.cookieSameSite)
                                            .maxAge(0)
                                            .path("/")
                                            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH-TOKEN", null)
                                            .httpOnly(true)
                                            .secure(true)
                                            .sameSite(this.cookieSameSite)
                                            .maxAge(0)
                                            .path("/api/auth")
                                            .build();

        return new CookiesResponseDTO(accessCookie, refreshCookie);
    }

}
