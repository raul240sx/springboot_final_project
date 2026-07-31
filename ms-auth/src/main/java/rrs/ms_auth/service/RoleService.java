package rrs.ms_auth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rrs.ms_auth.common.DTOMapper;
import rrs.ms_auth.common.exception.ResourceNotFoundException;
import rrs.ms_auth.dto.RoleRequestDTO;
import rrs.ms_auth.dto.RoleResponseDTO;
import rrs.ms_auth.model.Role;
import rrs.ms_auth.repository.IRoleRepository;





@Service
@Transactional(readOnly = true)
public class RoleService implements IRoleService{
    private final IRoleRepository roleRepository;
    private final DTOMapper dtoMapper;

    public RoleService(IRoleRepository roleRepository, DTOMapper dtoMapper) {
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
