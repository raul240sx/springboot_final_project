package com.store.sales_api.dto;

import java.util.List;

public record SaleRequestDTO(
    String clientCode,
    List<DetailRequestDTO> details
) {

}
