package com.store.sales_api.controller;

import java.time.LocalDate;
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

import com.store.sales_api.dto.SaleMajorAmountDTO;
import com.store.sales_api.dto.SaleRequestDTO;
import com.store.sales_api.dto.SaleResponseDTO;
import com.store.sales_api.dto.SaleSumCountDTO;
import com.store.sales_api.service.ISaleService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/sales")
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
    public ResponseEntity<Void> deleteSale(@PathVariable String saleCode) {
        saleService.deleteSale(saleCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sum-count/{date}")
    public ResponseEntity<SaleSumCountDTO> getDaySalesSumCount(@PathVariable LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getDaySaleSumCount(date));
    }

    @GetMapping("/best-sale")
    public ResponseEntity<SaleMajorAmountDTO> getBestSale() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getBestSale());
    }

}
