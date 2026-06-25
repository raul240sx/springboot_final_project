package com.store.sales_api.service;

import java.util.List;

import com.store.sales_api.dto.ProductRequestDTO;
import com.store.sales_api.dto.ProductResponseDTO;


public interface IProductService {

    List<ProductResponseDTO> getProducts();

    ProductResponseDTO getProduct(String productCode);

    ProductResponseDTO createProduct(ProductRequestDTO productDTO);

    ProductResponseDTO updateProduct(String productCode, ProductRequestDTO productDTO);

    void deleteProduct(String productCode);

    List<ProductResponseDTO> findLowStockProducts(Integer lessThanStock);
}
