package rrs.ms_products.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import rrs.ms_products.dto.DetailsRequestDTO;
import rrs.ms_products.dto.DetailsResponseDTO;
import rrs.ms_products.dto.ProductCategoryDTO;
import rrs.ms_products.dto.ProductRequestDTO;
import rrs.ms_products.dto.ProductResponseDTO;
import rrs.ms_products.service.IProductService;


@Controller
@Validated
@RequestMapping("/api/products")
public class ProductController {
    private IProductService productService;

    private final AtomicInteger contador = new AtomicInteger(0);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO));
    }

    @PutMapping("/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
        @PathVariable String productCode,
        @Valid @RequestBody ProductRequestDTO productDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productCode, productDTO));
    }

    @DeleteMapping("/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PostMapping("/validate-products-reduce-stock")
    public ResponseEntity<List<DetailsResponseDTO>> validateProductsAndReduceStock(@RequestBody List<@Valid DetailsRequestDTO> dto) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.validateProducts(dto));
    }

    @PostMapping("/return-stock")
    public ResponseEntity<Void> returnStock(@RequestBody List<@Valid DetailsRequestDTO> dto) {
        productService.returnStock(dto);
        return ResponseEntity.noContent().build();
        
    }

    @GetMapping("/test-retry")
    public ResponseEntity<String> testRetry() {
        int intento = contador.incrementAndGet();
        
        if (intento < 3) {
            // Simula el escenario que ya armamos: un ProblemDetail con 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Fallo simulado, intento " + intento);
        }
        
        return ResponseEntity.ok("Éxito en el intento " + intento);
    }

    @PostMapping("/test/breaker")
    public ResponseEntity<String> testCircuitBreaker() {
               
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Prueba de fallo");
    }
    
    
}
