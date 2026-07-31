package rrs.ms_products.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rrs.ms_products.common.DTOMapper;
import rrs.ms_products.common.ProductCodeGenerator;
import rrs.ms_products.common.exception.BusinessRuleException;
import rrs.ms_products.common.exception.ResourceNotFoundException;
import rrs.ms_products.dto.DetailsRequestDTO;
import rrs.ms_products.dto.DetailsResponseDTO;
import rrs.ms_products.dto.ProductCategoryDTO;
import rrs.ms_products.dto.ProductRequestDTO;
import rrs.ms_products.dto.ProductResponseDTO;
import rrs.ms_products.model.Product;
import rrs.ms_products.model.ProductCategory;
import rrs.ms_products.repository.IProductRepository;



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


    @Override
    @Transactional
    public List<DetailsResponseDTO> validateProducts(List<DetailsRequestDTO> detailsDTO) {
        //Busqueda de productos
        List<String> productCodes = detailsDTO.stream().map(detail -> detail.productCode()).toList();
        List<Product> products = productRepository.findAllByCodeIn(productCodes);
        Map<String, Product> productsStock = products.stream().collect(Collectors.toMap(product -> product.getCode(), product -> product));

        List<DetailsResponseDTO> responseDTO = new ArrayList<DetailsResponseDTO>();

        //Validación de existencia y reducción de stock
        for (DetailsRequestDTO detail : detailsDTO) {
            Product product = productsStock.get(detail.productCode());
            if (product == null) throw new ResourceNotFoundException("El producto con código " + detail.productCode() + " No se encuentra en los registros");

            if (product.getStock() < detail.quantity()) {
                throw new BusinessRuleException("Stock insuficiente del producto " + product.getCode() + " , maxima cantidad posible: " + product.getStock());
            }
            else {
                product.setStock(product.getStock() - detail.quantity());
            }
            responseDTO.add(dtoMapper.detailToDTO(
                product.getCode(),
                detail.quantity(),
                product.getPrice().multiply(BigDecimal.valueOf(detail.quantity()))
                )
            );
        }
        return responseDTO;
    }


    @Override
    @Transactional
    public void returnStock(List<DetailsRequestDTO> detailsDTO) {
        //Busqueda de productos
        List<String> productCodes = detailsDTO.stream().map(detail -> detail.productCode()).toList();
        List<Product> products = productRepository.findAllByCodeIn(productCodes);
        Map<String, Product> productsStock = products.stream().collect(Collectors.toMap(product -> product.getCode(), product -> product));

        //Validación de existencia y retorno de stock
        for (DetailsRequestDTO detail : detailsDTO) {
            Product product = productsStock.get(detail.productCode());
            if (product == null) throw new ResourceNotFoundException("El producto con código " + detail.productCode() + " No se encuentra en los registros");

            product.setStock(product.getStock() + detail.quantity());
        }
    }
}
