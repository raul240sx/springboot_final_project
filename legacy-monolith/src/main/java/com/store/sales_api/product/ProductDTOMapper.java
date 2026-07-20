package com.store.sales_api.product;

import org.springframework.stereotype.Component;

import com.store.sales_api.product.dto.ProductCategoryDTO;
import com.store.sales_api.product.dto.ProductResponseDTO;


@Component
public class ProductDTOMapper {


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
}
