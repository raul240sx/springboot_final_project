package com.store.sales_api.security.service;

import java.util.List;

import com.store.sales_api.security.dto.VendorChangePasswordDTO;
import com.store.sales_api.security.dto.VendorRequestDTO;
import com.store.sales_api.security.dto.VendorResponseDTO;


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
