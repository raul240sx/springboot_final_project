package rrs.ms_products.service;

import java.util.List;

import rrs.ms_products.dto.DetailsRequestDTO;
import rrs.ms_products.dto.DetailsResponseDTO;
import rrs.ms_products.dto.ProductCategoryDTO;
import rrs.ms_products.dto.ProductRequestDTO;
import rrs.ms_products.dto.ProductResponseDTO;


public interface IProductService {

    List<ProductResponseDTO> getProducts();

    ProductResponseDTO getProduct(String productCode);

    ProductResponseDTO createProduct(ProductRequestDTO productDTO);

    ProductResponseDTO updateProduct(String productCode, ProductRequestDTO productDTO);

    void deleteProduct(String productCode);

    List<ProductResponseDTO> findLowStockProducts(Integer lessThanStock);

    List<ProductCategoryDTO> getCategoryProducts();

    List<DetailsResponseDTO> validateProducts(List<DetailsRequestDTO> detailsDTO);

    void returnStock(List<DetailsRequestDTO> detailsDTO);
}
