package rrs.ms_sales.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SaleRequestDTO(

    @NotBlank(message = "{sale.client.code.null}")
    String clientCode,

    @NotBlank(message = "{sale.vendor.code.null}")
    String vendorCode,

    @NotEmpty(message = "{sale.details.empty}")
    List<@Valid DetailRequestDTO> details
) {

}
