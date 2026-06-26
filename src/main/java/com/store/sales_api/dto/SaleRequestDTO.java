package com.store.sales_api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SaleRequestDTO(

    @NotBlank(message = "{sale.client.code.null}")
    String clientCode,

    @NotEmpty(message = "{sale.details.empty}")
    @Valid
    List<DetailRequestDTO> details
) {

}
