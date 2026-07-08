package com.store.sales_api.product;

import java.util.List;

import com.store.sales_api.product.dto.ProductCategoryDTO;
import com.store.sales_api.product.dto.ProductRequestDTO;
import com.store.sales_api.product.dto.ProductResponseDTO;


public interface IProductService {

    List<ProductResponseDTO> getProducts();

    ProductResponseDTO getProduct(String productCode);

    ProductResponseDTO createProduct(ProductRequestDTO productDTO);

    ProductResponseDTO updateProduct(String productCode, ProductRequestDTO productDTO);

    void deleteProduct(String productCode);

    List<ProductResponseDTO> findLowStockProducts(Integer lessThanStock);

    List<ProductCategoryDTO> getCategoryProducts();
}
