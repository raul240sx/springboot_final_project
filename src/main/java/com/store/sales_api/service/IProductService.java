package com.store.sales_api.service;

import java.util.List;

import com.store.sales_api.dto.ProductRequestDTO;
import com.store.sales_api.dto.ProductResponseDTO;


public interface IProductService {

    List<ProductResponseDTO> getProducts();

    ProductResponseDTO getProduct(Long productId);

    ProductResponseDTO createProduct(ProductRequestDTO productDTO);

    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productDTO);

    void deleteProduct(Long productId);
}
