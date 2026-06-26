package com.store.sales_api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.dto.ProductCategoryDTO;
import com.store.sales_api.dto.ProductRequestDTO;
import com.store.sales_api.dto.ProductResponseDTO;
import com.store.sales_api.exception.ResourceNotFoundException;
import com.store.sales_api.mapper.DTOMapper;
import com.store.sales_api.model.Product;
import com.store.sales_api.model.ProductCategory;
import com.store.sales_api.repository.IProductRepository;
import com.store.sales_api.util.ProductCodeGenerator;


@Service
@Transactional(readOnly = true)
public class ProductService implements IProductService{
    private final IProductRepository productRepository;
    private final DTOMapper dtoMapper;
    private final ProductCodeGenerator codeGenerator;


    public ProductService(IProductRepository productRepository, DTOMapper dtoMapper, ProductCodeGenerator codeGenerator) {
        this.productRepository = productRepository;
        this.dtoMapper = dtoMapper;
        this.codeGenerator = codeGenerator;
    }



    @Override
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().map(product -> dtoMapper.productToDTO(product)).toList();
    }

    @Override
    public ProductResponseDTO getProduct(String productCode) {
        Product product = productRepository.findByCode(productCode).orElseThrow(() -> new ResourceNotFoundException("El producto buscado no existe"));

        return dtoMapper.productToDTO(product);
    }                                                                                  

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productDTO) {
        Product newProduct = new Product(productDTO.name(), productDTO.brand(), productDTO.category(), productDTO.price(), productDTO.stock());

        Product createdProduct = productRepository.save(newProduct);

        createdProduct.setCode(codeGenerator.generateProductCode(createdProduct));

        return dtoMapper.productToDTO(createdProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(String productCode, ProductRequestDTO productDTO) {
        Product productToEdit = productRepository.findByCode(productCode).orElseThrow(() -> new ResourceNotFoundException("El producto seleccionado para editar no existe"));

        productToEdit.setName(productDTO.name());
        productToEdit.setBrand(productDTO.brand());
        productToEdit.setPrice(productDTO.price());
        productToEdit.setStock(productDTO.stock());

        if (productToEdit.getCategory() != productDTO.category()) {
            productToEdit.setCategory(productDTO.category());
            productToEdit.setCode(codeGenerator.generateProductCode(productToEdit));
        }

        return dtoMapper.productToDTO(productToEdit);

    }

    @Override
    @Transactional
    public void deleteProduct(String productCode) {
        Product productToDelete = productRepository.findByCode(productCode).orElseThrow(() -> new ResourceNotFoundException("El producto seleccionado para eliminar no existe"));

        productRepository.delete(productToDelete);
    }



    @Override
    public List<ProductResponseDTO> findLowStockProducts(Integer lessThanStock) {
        List<Product> lowStockProducts = productRepository.findByStockLessThan(lessThanStock);

        return lowStockProducts.stream().map(product -> dtoMapper.productToDTO(product)).toList();
    }



    @Override
    public List<ProductCategoryDTO> getCategoryProducts() {
        ProductCategory[] categories = ProductCategory.values();

        return Arrays.stream(categories).map(category -> new ProductCategoryDTO(category.name(), category.getEsp())).toList();
    }
    

}
