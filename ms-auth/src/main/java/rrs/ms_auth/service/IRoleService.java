package rrs.ms_auth.service;

import java.util.List;


import rrs.ms_auth.dto.RoleRequestDTO;
import rrs.ms_auth.dto.RoleResponseDTO;
import rrs.ms_auth.model.Role;



public interface IRoleService {
    
    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRole(Long id);

    RoleResponseDTO createRole(RoleRequestDTO dto);

    void deleteRole(Long id);

    Role findRole(Long id);
}
