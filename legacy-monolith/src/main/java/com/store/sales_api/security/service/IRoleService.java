package com.store.sales_api.security.service;

import java.util.List;

import com.store.sales_api.security.Role;
import com.store.sales_api.security.dto.RoleRequestDTO;
import com.store.sales_api.security.dto.RoleResponseDTO;


public interface IRoleService {
    
    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRole(Long id);

    RoleResponseDTO createRole(RoleRequestDTO dto);

    void deleteRole(Long id);

    Role findRole(Long id);
}
