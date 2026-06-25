package com.store.sales_api.model;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@SQLDelete(sql = "UPDATE product SET is_active=false WHERE id=?")
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private BigDecimal price = new BigDecimal(0.00);
    private Integer stock = 0;
    private Boolean isActive = true;    
    
    
    public Product() {
    }

    public Product(String name, String brand, ProductCategory category, BigDecimal price, Integer stock) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

}
