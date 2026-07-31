package rrs.ms_products.common;

import org.springframework.stereotype.Component;

import rrs.ms_products.model.Product;


@Component
public class ProductCodeGenerator {

    public String generateProductCode(Product product) {
        
        String idZeros =  String.format("%05d", product.getId());
        return product.getCategory().getPrefix() + "-" + idZeros;
    }

}
