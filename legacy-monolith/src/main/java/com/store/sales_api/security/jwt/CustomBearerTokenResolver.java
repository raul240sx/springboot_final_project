package com.store.sales_api.security.jwt;

import java.util.Arrays;

import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class CustomBearerTokenResolver implements BearerTokenResolver{

    @Override
    public @Nullable String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        
        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("AUTH-TOKEN")).findFirst().orElse(null);
        if (cookie == null) return null;

        return cookie.getValue();
    }

}
