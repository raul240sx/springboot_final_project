package rrs.ms_sales.common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import rrs.ms_sales.dto.ClientDTO;
import rrs.ms_sales.dto.DetailRequestDTO;
import rrs.ms_sales.dto.DetailResponseDTO;
import rrs.ms_sales.dto.SaleMajorAmountDTO;
import rrs.ms_sales.dto.SaleResponseDTO;
import rrs.ms_sales.dto.SaleSumCountDTO;
import rrs.ms_sales.model.Detail;
import rrs.ms_sales.model.Sale;



@Component
public class DTOMapper {
    
    public DTOMapper(){

    }

    
    public DetailResponseDTO detailToDTO(Detail detail) {
        if (detail == null) return null;

        return new DetailResponseDTO(
            detail.getProductCode(),
            detail.getQuantity(),
            detail.getPartialAmount()
        );
    }

    public DetailRequestDTO detailRequestToDTO(Detail detail) {
        if (detail == null) return null;

        return new DetailRequestDTO(
            detail.getProductCode(),
            detail.getQuantity()
        );
    }


    public SaleResponseDTO saleToDTO(Sale sale) {
        if (sale == null) return null;

        List<DetailResponseDTO> details = sale.getDetails().stream().map(detail -> detailToDTO(detail)).toList();

        return new SaleResponseDTO(
            sale.getCode(),
            sale.getDate(),
            sale.getTotalAmount(),
            sale.getClientCode(),
            details
        );
    }

    public SaleSumCountDTO saleSumCounToDTO(LocalDate date, Integer salesCount, BigDecimal dayTotalAmount) {
        if (date == null || salesCount == null || dayTotalAmount == null) return null;

        return new SaleSumCountDTO(date, salesCount, dayTotalAmount);
    }

    
    public SaleMajorAmountDTO saleMajorAmountToDTO(Sale sale, ClientDTO client) {
        if (sale == null) return null;

        List<DetailResponseDTO> details = sale.getDetails().stream().map(detail -> this.detailToDTO(detail)).toList();

        return new SaleMajorAmountDTO(
            sale.getCode(),
            sale.getTotalAmount(),
            client.code(),
            client.name(),
            client.lastName(),
            details
        );
    }


}
