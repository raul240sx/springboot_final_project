package com.store.sales_api.product;

import org.springframework.stereotype.Component;


@Component
public class ProductCodeGenerator {

    public String generateProductCode(Product product) {
        
        String idZeros =  String.format("%05d", product.getId());
        return product.getCategory().getPrefix() + "-" + idZeros;
    }

}
