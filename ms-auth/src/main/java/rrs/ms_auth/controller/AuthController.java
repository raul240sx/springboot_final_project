package rrs.ms_auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import rrs.ms_auth.dto.CookiesResponseDTO;
import rrs.ms_auth.dto.LoginRequestDTO;
import rrs.ms_auth.service.IAuthService;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;


    public AuthController(IAuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDTO dto){

        CookiesResponseDTO cookies = authService.login(dto);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookies.accessCookie().toString()).header(HttpHeaders.SET_COOKIE, cookies.refreshCookie().toString()).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = "REFRESH-TOKEN") String refreshToken) {
        
        CookiesResponseDTO cookies = authService.refresh(refreshToken);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookies.accessCookie().toString()).header(HttpHeaders.SET_COOKIE, cookies.refreshCookie().toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "REFRESH-TOKEN", required = false) String refreshToken) {

        CookiesResponseDTO cookies = authService.logout(refreshToken);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookies.accessCookie().toString()).header(HttpHeaders.SET_COOKIE, cookies.refreshCookie().toString()).build();
    }

}
