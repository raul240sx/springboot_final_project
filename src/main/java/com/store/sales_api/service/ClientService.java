package com.store.sales_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.dto.ClientRequestDTO;
import com.store.sales_api.dto.ClientResponseDTO;
import com.store.sales_api.exception.ResourceNotFoundException;
import com.store.sales_api.mapper.DTOMapper;
import com.store.sales_api.model.Client;
import com.store.sales_api.repository.IClientRepository;


@Service
@Transactional(readOnly = true)
public class ClientService implements IClientService{
    private final IClientRepository clientRepository;
    private final DTOMapper dtoMapper;

    public ClientService(IClientRepository clientRepository, DTOMapper dtoMapper) {
        this.clientRepository = clientRepository;
        this.dtoMapper = dtoMapper;
    }


    @Override
    public List<ClientResponseDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> dtoMapper.clientToDTO(client)).toList();
    }

    @Override
    public ClientResponseDTO getClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("El cliente buscado no existe"));

        return dtoMapper.clientToDTO(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientDTO) {
        Client newClient = new Client(clientDTO.name(), clientDTO.lastName(), clientDTO.dni());

        Client createdClient = clientRepository.save(newClient);

        return dtoMapper.clientToDTO(createdClient);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(Long clientId, ClientRequestDTO clientDTO) {
        Client clientToUpdate = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("El cliente buscado para editar no existe"));

        clientToUpdate.setName(clientDTO.name());
        clientToUpdate.setLastName(clientDTO.lastName());
        clientToUpdate.setDni(clientDTO.dni());

        clientRepository.save(clientToUpdate);

        return dtoMapper.clientToDTO(clientToUpdate);
    }

    @Override
    @Transactional
    public void deleteClient(Long clientId) {
        Client clientToDelete = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("El cliente buscado para eliminar no existe"));

        clientRepository.delete(clientToDelete);
    }

}
