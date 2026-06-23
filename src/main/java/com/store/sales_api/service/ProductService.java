package com.store.sales_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.dto.ProductRequestDTO;
import com.store.sales_api.dto.ProductResponseDTO;
import com.store.sales_api.exception.ResourceNotFoundException;
import com.store.sales_api.mapper.DTOMapper;
import com.store.sales_api.model.Product;
import com.store.sales_api.repository.IProductRepository;


@Service
@Transactional(readOnly = true)
public class ProductService implements IProductService{
    private final IProductRepository productRepository;
    private final DTOMapper dtoMapper;


    public ProductService(IProductRepository productRepository, DTOMapper dtoMapper) {
        this.productRepository = productRepository;
        this.dtoMapper = dtoMapper;
    }



    @Override
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().map(product -> dtoMapper.productToDTO(product)).toList();
    }

    @Override
    public ProductResponseDTO getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("El producto buscado no existe"));

        return dtoMapper.productToDTO(product);
    }                                                                                  

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productDTO) {
        Product newProduct = new Product(productDTO.code(), productDTO.name(), productDTO.brand(), productDTO.price(), productDTO.stock());

        Product createdProduct = productRepository.save(newProduct);

        return dtoMapper.productToDTO(createdProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productDTO) {
        Product productToEdit = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("El producto seleccionado para editar no existe"));

        productToEdit.setCode(productDTO.code());
        productToEdit.setName(productDTO.name());
        productToEdit.setBrand(productDTO.brand());
        productToEdit.setPrice(productDTO.price());
        productToEdit.setStock(productDTO.stock());

        productRepository.save(productToEdit);

        return dtoMapper.productToDTO(productToEdit);

    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("El producto seleccionado para eliminar no existe"));

        productRepository.delete(productToDelete);
    }

}
