package com.store.sales_api.util;

import org.springframework.stereotype.Component;

import com.store.sales_api.model.Product;


@Component
public class ProductCodeGenerator {

    public String generateProductCode(Product product) {
        
        String idZeros =  String.format("%05d", product.getId());
        return product.getCategory().getPrefix() + "-" + idZeros;
    }

}
