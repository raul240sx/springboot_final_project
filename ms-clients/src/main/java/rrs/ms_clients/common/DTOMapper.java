package rrs.ms_clients.common;

import org.springframework.stereotype.Component;

import rrs.ms_clients.dto.ClientResponseDTO;
import rrs.ms_clients.model.Client;



@Component
public class DTOMapper {


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
