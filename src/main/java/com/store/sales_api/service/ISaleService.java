package com.store.sales_api.service;

import java.util.List;

import com.store.sales_api.dto.DetailRequestDTO;
import com.store.sales_api.dto.SaleRequestDTO;
import com.store.sales_api.dto.SaleResponseDTO;

public interface ISaleService {

    List<SaleResponseDTO> getSales();

    SaleResponseDTO getSale(String saleCode);

    SaleResponseDTO createSale(SaleRequestDTO saleDTO);

    SaleResponseDTO updateSale(String saleCode, SaleRequestDTO saleDTO);

    void deleteSale(String saleCode);
}
