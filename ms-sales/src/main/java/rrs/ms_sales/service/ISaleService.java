package rrs.ms_sales.service;

import java.time.LocalDate;
import java.util.List;

import rrs.ms_sales.dto.SaleMajorAmountDTO;
import rrs.ms_sales.dto.SaleRequestDTO;
import rrs.ms_sales.dto.SaleResponseDTO;
import rrs.ms_sales.dto.SaleSumCountDTO;


public interface ISaleService {

    List<SaleResponseDTO> getSales();

    SaleResponseDTO getSale(String saleCode);

    SaleResponseDTO createSale(SaleRequestDTO saleDTO);

    SaleResponseDTO updateSale(String saleCode, SaleRequestDTO saleDTO);

    void deleteSale(String saleCode);

    SaleSumCountDTO getDaySaleSumCount(LocalDate date);

    SaleMajorAmountDTO getBestSale();
}
