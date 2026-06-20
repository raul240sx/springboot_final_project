package com.store.sales_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stock;
    
    
    
    public Product() {
    }

    public Product(String code, String name, String brand, BigDecimal price, Integer stock) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }

}
