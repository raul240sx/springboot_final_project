package com.store.sales_api.client;

import com.store.sales_api.client.dto.ClientResponseDTO;

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
