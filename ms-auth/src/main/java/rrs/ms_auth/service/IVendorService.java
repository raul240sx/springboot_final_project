package rrs.ms_auth.service;

import java.util.List;

import rrs.ms_auth.dto.VendorChangePasswordDTO;
import rrs.ms_auth.dto.VendorRequestDTO;
import rrs.ms_auth.dto.VendorResponseDTO;


public interface IVendorService {

    VendorResponseDTO createVendor(VendorRequestDTO dto);

    VendorResponseDTO getVendor(String code);

    List<VendorResponseDTO> getAllVendors();

    VendorResponseDTO updateVendor(String code, VendorRequestDTO dto);

    void deleteVendor(String requestUserCode, String code);

    VendorResponseDTO assignRole(String code, Long roleId);

    VendorResponseDTO removeRole(String code, Long roleId);

    VendorResponseDTO changePassword(String code, VendorChangePasswordDTO dto);

}
