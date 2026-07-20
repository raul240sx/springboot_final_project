package com.store.sales_api.client;

import org.springframework.stereotype.Component;

import com.store.sales_api.client.dto.ClientResponseDTO;


@Component
public class ClientDTOMapper {


    public ClientResponseDTO clientToDTO(Client client) {
        if (client == null) return null;

        return new ClientResponseDTO(
            client.getCode(),
            client.getName(),
            client.getLastName(),
            client.getDni()
        );
    }

}
