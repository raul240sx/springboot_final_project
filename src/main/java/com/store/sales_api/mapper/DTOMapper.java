package com.store.sales_api.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.store.sales_api.dto.ClientResponseDTO;
import com.store.sales_api.dto.DetailResponseDTO;
import com.store.sales_api.dto.ProductResponseDTO;
import com.store.sales_api.dto.SaleResponseDTO;
import com.store.sales_api.dto.SaleSumCountDTO;
import com.store.sales_api.model.Client;
import com.store.sales_api.model.Detail;
import com.store.sales_api.model.Product;
import com.store.sales_api.model.Sale;


@Component
public class DTOMapper {
    
    public DTOMapper(){

    }

    public ProductResponseDTO productToDTO(Product product){
        if (product == null) return null;

        return new ProductResponseDTO(
        product.getCode(),
        product.getName(),
        product.getBrand(),
        product.getCategory().toString(),
        product.getPrice(),
        product.getStock()
    );
    }


    public ClientResponseDTO clientToDTO(Client client) {
        if (client == null) return null;

        return new ClientResponseDTO(
            client.getCode(),
            client.getName(),
            client.getLastName(),
            client.getDni()
        );
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
            sale.getClient().getId(),
            details
        );
    }

    public SaleSumCountDTO saleSumCounToDTO(LocalDate date, Integer salesCount, BigDecimal dayTotalAmount) {
        if (date == null || salesCount == null || dayTotalAmount == null) return null;

        return new SaleSumCountDTO(date, salesCount, dayTotalAmount);
    }
}
