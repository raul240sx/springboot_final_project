package rrs.ms_products.common;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import rrs.ms_products.dto.DetailsResponseDTO;
import rrs.ms_products.dto.ProductCategoryDTO;
import rrs.ms_products.dto.ProductResponseDTO;
import rrs.ms_products.model.Product;


@Component
public class DTOMapper {


    public ProductCategoryDTO categoryToDTO(String categoryCode, String categoryLabel) {
        if (categoryCode == null || categoryLabel == null) return null;

        return new ProductCategoryDTO(categoryCode, categoryLabel);
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

    public DetailsResponseDTO detailToDTO(String productCode, Integer quantity, BigDecimal partialAmount) {
        return new DetailsResponseDTO(
            productCode,
            quantity,
            partialAmount
        );
    }
}
