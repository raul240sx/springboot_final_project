package com.store.sales_api.client;

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

import com.store.sales_api.client.dto.ClientRequestDTO;
import com.store.sales_api.client.dto.ClientResponseDTO;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/api/clients")
public class ClientController {
    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getClients());
    }

    @GetMapping("/{clientCode}")
    public ResponseEntity<ClientResponseDTO> getclient(@PathVariable String clientCode) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getClient(clientCode));
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO clientDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientDTO));
    }

    @PutMapping("/{clientCode}")
    public ResponseEntity<ClientResponseDTO> updateClient(
        @PathVariable String clientCode,
        @Valid @RequestBody ClientRequestDTO clientDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateClient(clientCode, clientDTO));
    }

    @DeleteMapping("/{clientCode}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientCode) {
        clientService.deleteClient(clientCode);
        return ResponseEntity.noContent().build();
    }

}
