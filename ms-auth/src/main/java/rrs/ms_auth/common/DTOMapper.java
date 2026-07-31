package rrs.ms_auth.common;

import org.springframework.stereotype.Component;

import rrs.ms_auth.dto.RefreshTokenResponseDTO;
import rrs.ms_auth.dto.RoleResponseDTO;
import rrs.ms_auth.dto.VendorResponseDTO;
import rrs.ms_auth.model.RefreshToken;
import rrs.ms_auth.model.Role;
import rrs.ms_auth.model.Vendor;



@Component
public class DTOMapper {


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
