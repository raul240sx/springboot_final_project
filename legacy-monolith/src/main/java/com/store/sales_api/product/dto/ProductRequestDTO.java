package com.store.sales_api.product.dto;

import java.math.BigDecimal;

import com.store.sales_api.product.ProductCategory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;



public record ProductRequestDTO(

    
    @NotBlank(message = "{product.name.null}")
    String name,

    @NotBlank(message = "{product.brand.null}")
    String brand,

    @NotNull(message = "{product.category.null}")
    ProductCategory category,

    @NotNull(message = "{product.price.null}")
    @DecimalMin(value = "0.00", message = "{product.price.min.value.error}")
    BigDecimal price,

    @NotNull(message = "{product.stock.null}")
    @PositiveOrZero(message = "{product.stock.min.value}") 
    Integer stock
) {

}
