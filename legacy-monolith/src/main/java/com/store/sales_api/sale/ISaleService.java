package com.store.sales_api.sale;

import java.time.LocalDate;
import java.util.List;

import com.store.sales_api.sale.dto.SaleMajorAmountDTO;
import com.store.sales_api.sale.dto.SaleRequestDTO;
import com.store.sales_api.sale.dto.SaleResponseDTO;
import com.store.sales_api.sale.dto.SaleSumCountDTO;

public interface ISaleService {

    List<SaleResponseDTO> getSales();

    SaleResponseDTO getSale(String saleCode);

    SaleResponseDTO createSale(SaleRequestDTO saleDTO);

    SaleResponseDTO updateSale(String saleCode, SaleRequestDTO saleDTO);

    void deleteSale(String saleCode);

    SaleSumCountDTO getDaySaleSumCount(LocalDate date);

    SaleMajorAmountDTO getBestSale();
}
