package com.store.sales_api.dto;

import java.util.List;

public record SaleRequestDTO(
    Long clientId,
    List<DetailRequestDTO> details
) {

}
