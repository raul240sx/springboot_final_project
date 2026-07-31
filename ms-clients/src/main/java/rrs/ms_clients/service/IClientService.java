package rrs.ms_clients.service;

import java.util.List;

import rrs.ms_clients.dto.ClientRequestDTO;
import rrs.ms_clients.dto.ClientResponseDTO;


public interface IClientService {

    List<ClientResponseDTO> getClients();

    ClientResponseDTO getClient(String clientCode);

    ClientResponseDTO createClient(ClientRequestDTO clientDTO);

    ClientResponseDTO updateClient(String clientCode, ClientRequestDTO clientDTO);

    void deleteClient(String clientCode);
}
