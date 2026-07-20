package com.store.sales_api.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record VendorAssignDeleteRol(

    @NotBlank(message = "${vendor.request.vendor-code.null}")
    String vendorCode,

    @NotNull(message = "${vendor.request.role-id.null}")
    Long roleId
) {

}
