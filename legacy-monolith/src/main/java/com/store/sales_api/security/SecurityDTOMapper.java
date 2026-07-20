package com.store.sales_api.security;

import org.springframework.stereotype.Component;

import com.store.sales_api.security.dto.RefreshTokenResponseDTO;
import com.store.sales_api.security.dto.RoleResponseDTO;
import com.store.sales_api.security.dto.VendorResponseDTO;


@Component
public class SecurityDTOMapper {


    public VendorResponseDTO vendorToDTO(Vendor vendor) {
        if (vendor == null) return null;


        return new VendorResponseDTO(
            vendor.getCode(),
            vendor.getName(),
            vendor.getLastName(),
            vendor.getDni(),
            vendor.getRoles().stream().map(vendorRole -> vendorRole.getRole().getName()).toList()
        );
    }


    public RoleResponseDTO roleToDTO(Role role) {
        if (role == null) return null;

        return new RoleResponseDTO(role.getId(), role.getName());
    }

    public RefreshTokenResponseDTO refreshTokenToDTO(RefreshToken refreshToken, String rawToken) {
        if (refreshToken == null || rawToken == null) return null;

        return new RefreshTokenResponseDTO(rawToken, refreshToken.getVendor().getCode(), refreshToken.getExpiryDate());
    }

}
