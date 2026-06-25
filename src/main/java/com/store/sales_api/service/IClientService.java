package com.store.sales_api.service;

import java.util.List;

import com.store.sales_api.dto.ClientRequestDTO;
import com.store.sales_api.dto.ClientResponseDTO;

public interface IClientService {

    List<ClientResponseDTO> getClients();

    ClientResponseDTO getClient(String clientCode);

    ClientResponseDTO createClient(ClientRequestDTO clientDTO);

    ClientResponseDTO updateClient(String clientCode, ClientRequestDTO clientDTO);

    void deleteClient(String clientCode);
}
