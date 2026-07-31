package rrs.ms_sales.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import rrs.ms_sales.dto.SaleMajorAmountDTO;
import rrs.ms_sales.dto.SaleRequestDTO;
import rrs.ms_sales.dto.SaleResponseDTO;
import rrs.ms_sales.dto.SaleSumCountDTO;
import rrs.ms_sales.service.ISaleService;


@Controller
@Validated
@RequestMapping("/api/sales")
public class SaleController {
    private final ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }


    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAllSales() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getSales());
    }

    @GetMapping("/{saleCode}")
    public ResponseEntity<SaleResponseDTO> getSale(@PathVariable String saleCode) {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getSale(saleCode));
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleRequestDTO saleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(saleDTO));
    }

    @PutMapping("/{saleCode}")
    public ResponseEntity<SaleResponseDTO> updateSale(
        @PathVariable String saleCode,
        @Valid @RequestBody SaleRequestDTO saleDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.updateSale(saleCode, saleDTO));
    }

    @DeleteMapping("/{saleCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSale(@PathVariable String saleCode) {
        saleService.deleteSale(saleCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sum-count/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleSumCountDTO> getDaySalesSumCount(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getDaySaleSumCount(date));
    }

    @GetMapping("/best-sale")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleMajorAmountDTO> getBestSale() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getBestSale());
    }

}
