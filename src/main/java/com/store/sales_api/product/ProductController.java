package com.store.sales_api.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.store.sales_api.product.dto.ProductCategoryDTO;
import com.store.sales_api.product.dto.ProductRequestDTO;
import com.store.sales_api.product.dto.ProductResponseDTO;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/api/products")
public class ProductController {
    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable String productCode) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(productCode));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO));
    }

    @PutMapping("/{productCode}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
        @PathVariable String productCode,
        @Valid @RequestBody ProductRequestDTO productDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productCode, productDTO));
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productCode) {
        productService.deleteProduct(productCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock/{lessThanStock}")
    public ResponseEntity<List<ProductResponseDTO>> findLowStockProducts(@PathVariable Integer lessThanStock) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findLowStockProducts(lessThanStock));
    }

    @GetMapping("/get-categories")
    public ResponseEntity<List<ProductCategoryDTO>> getCategoryProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getCategoryProducts());
    }

}
