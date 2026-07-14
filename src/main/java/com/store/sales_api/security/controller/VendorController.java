package com.store.sales_api.security.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.store.sales_api.common.exception.BusinessRuleException;
import com.store.sales_api.security.dto.VendorAssignDeleteRol;
import com.store.sales_api.security.dto.VendorChangePasswordDTO;
import com.store.sales_api.security.dto.VendorRequestDTO;
import com.store.sales_api.security.dto.VendorResponseDTO;
import com.store.sales_api.security.service.IVendorService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/api/vendor")
public class VendorController {
    private final IVendorService vendorService;

    public VendorController(IVendorService vendorService) {
        this.vendorService = vendorService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VendorResponseDTO>> getVendors() {
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.getAllVendors());
    }

    @GetMapping("/{vendorCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> getVendor(@PathVariable String vendorCode) {
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.getVendor(vendorCode));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> createVendor(@Valid @RequestBody VendorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.createVendor(dto));
    }

    @PutMapping("/{vendorCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> updateVendor(@PathVariable String vendorCode, @Valid @RequestBody VendorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.updateVendor(vendorCode, dto));
    }

    @DeleteMapping("/{vendorCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVendor(Principal principal, @PathVariable String vendorCode) {
        vendorService.deleteVendor(principal.getName(), vendorCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/add-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> assignRole(@Valid @RequestBody VendorAssignDeleteRol dto) {
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.assignRole(dto.vendorCode(), dto.roleId()));
    }

    @PutMapping("/remove-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> removeRole(@Valid @RequestBody VendorAssignDeleteRol dto) {
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.removeRole(dto.vendorCode(), dto.roleId()));
    }

    @PutMapping("/change-password/{vendorCode}")
    public ResponseEntity<VendorResponseDTO> changePassword(@PathVariable String vendorCode, @Valid @RequestBody VendorChangePasswordDTO dto, Principal principal) {
        if (!vendorCode.equals(principal.getName())) throw new BusinessRuleException("No se tienen los permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.changePassword(vendorCode, dto));
    }
}
