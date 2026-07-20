package com.store.sales_api.security.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.common.exception.ResourceNotFoundException;
import com.store.sales_api.security.IRoleRepository;
import com.store.sales_api.security.Role;
import com.store.sales_api.security.SecurityDTOMapper;
import com.store.sales_api.security.dto.RoleRequestDTO;
import com.store.sales_api.security.dto.RoleResponseDTO;




@Service
@Transactional(readOnly = true)
public class RoleService implements IRoleService{
    private final IRoleRepository roleRepository;
    private final SecurityDTOMapper dtoMapper;

    public RoleService(IRoleRepository roleRepository, SecurityDTOMapper dtoMapper) {
        this.roleRepository = roleRepository;
        this.dtoMapper = dtoMapper;
    }


    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> dtoMapper.roleToDTO(role)).toList();
    }

    @Override
    public RoleResponseDTO getRole(Long id) {
        Role role =  roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El rol con id " + id + " no existe."));
        return dtoMapper.roleToDTO(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO dto) {
        Role newRole = new Role(dto.name());
        Role createdRole = roleRepository.save(newRole);

        return dtoMapper.roleToDTO(createdRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El rol con el id " + id + " no existe"));
        roleRepository.delete(role);
    }


    @Override
    public Role findRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El rol con id " + id + " no existe."));
    }


}
