package com.store.sales_api.product;

import lombok.Getter;

@Getter
public enum ProductCategory {
    GROCERIES("GRO", "Abarrotes"),
    BOOKSHOP("BOO", "Librería"),
    HOME("HOM", "Hogar"),
    TECHNOLOGY("TEC", "Tecnología");

    private final String prefix;
    private final String esp;

    private ProductCategory(String prefix, String esp){
        this.prefix = prefix;
        this.esp = esp;
    }
}
