package com.store.sales_api.sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.store.sales_api.sale.dto.DetailResponseDTO;
import com.store.sales_api.sale.dto.SaleMajorAmountDTO;
import com.store.sales_api.sale.dto.SaleResponseDTO;
import com.store.sales_api.sale.dto.SaleSumCountDTO;


@Component
public class SaleDTOMapper {
    
    public SaleDTOMapper(){

    }

    
    public DetailResponseDTO detailToDTO(Detail detail) {
        if (detail == null) return null;

        return new DetailResponseDTO(
            detail.getProduct().getCode(),
            detail.getQuantity(),
            detail.getPartialAmount()
        );
    }

    public SaleResponseDTO saleToDTO(Sale sale) {
        if (sale == null) return null;

        List<DetailResponseDTO> details = sale.getDetails().stream().map(detail -> detailToDTO(detail)).toList();

        return new SaleResponseDTO(
            sale.getCode(),
            sale.getDate(),
            sale.getTotalAmount(),
            sale.getClient().getCode(),
            details
        );
    }

    public SaleSumCountDTO saleSumCounToDTO(LocalDate date, Integer salesCount, BigDecimal dayTotalAmount) {
        if (date == null || salesCount == null || dayTotalAmount == null) return null;

        return new SaleSumCountDTO(date, salesCount, dayTotalAmount);
    }

    public SaleMajorAmountDTO saleMajorAmountToDTO(Sale sale,Integer productQuantity) {
        if (sale == null) return null;

        return new SaleMajorAmountDTO(
            sale.getCode(),
            sale.getTotalAmount(),
            productQuantity,
            sale.getClient().getName(),
            sale.getClient().getLastName(),
            sale.getClient().getCode()
        );
    }


}
