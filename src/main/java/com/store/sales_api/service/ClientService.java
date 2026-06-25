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
import com.store.sales_api.util.ClientCodeGenerator;


@Service
@Transactional(readOnly = true)
public class ClientService implements IClientService{
    private final IClientRepository clientRepository;
    private final DTOMapper dtoMapper;
    private final ClientCodeGenerator codeGenerator;

    public ClientService(IClientRepository clientRepository, DTOMapper dtoMapper, ClientCodeGenerator codeGenerator) {
        this.clientRepository = clientRepository;
        this.dtoMapper = dtoMapper;
        this.codeGenerator = codeGenerator;
    }


    @Override
    public List<ClientResponseDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> dtoMapper.clientToDTO(client)).toList();
    }

    @Override
    public ClientResponseDTO getClient(String clientCode) {
        Client client = clientRepository.findByCode(clientCode).orElseThrow(() -> new ResourceNotFoundException("El cliente buscado no existe"));

        return dtoMapper.clientToDTO(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientDTO) {
        Client newClient = new Client(clientDTO.name(), clientDTO.lastName(), clientDTO.dni());

        Client createdClient = clientRepository.save(newClient);
        
        createdClient.setCode(codeGenerator.generateSaleCode(createdClient.getId()));

        return dtoMapper.clientToDTO(createdClient);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(String clientCode, ClientRequestDTO clientDTO) {
        Client clientToUpdate = clientRepository.findByCode(clientCode).orElseThrow(() -> new ResourceNotFoundException("El cliente buscado para editar no existe"));

        clientToUpdate.setName(clientDTO.name());
        clientToUpdate.setLastName(clientDTO.lastName());
        clientToUpdate.setDni(clientDTO.dni());

        clientRepository.save(clientToUpdate);

        return dtoMapper.clientToDTO(clientToUpdate);
    }

    @Override
    @Transactional
    public void deleteClient(String clientCode) {
        Client clientToDelete = clientRepository.findByCode(clientCode).orElseThrow(() -> new ResourceNotFoundException("El cliente buscado para eliminar no existe"));

        clientRepository.delete(clientToDelete);
    }

}
