package com.store.sales_api.security.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.common.exception.BusinessRuleException;
import com.store.sales_api.common.exception.ResourceNotFoundException;
import com.store.sales_api.security.IVendorRepository;
import com.store.sales_api.security.Role;
import com.store.sales_api.security.SecurityDTOMapper;
import com.store.sales_api.security.Vendor;
import com.store.sales_api.security.VendorCodeGenerator;
import com.store.sales_api.security.VendorRole;
import com.store.sales_api.security.dto.VendorChangePasswordDTO;
import com.store.sales_api.security.dto.VendorRequestDTO;
import com.store.sales_api.security.dto.VendorResponseDTO;



@Service
@Transactional(readOnly = true)
public class VendorService implements IVendorService{
    private final IVendorRepository vendorRepository;
    private final VendorCodeGenerator codeGenerator;
    private final IRoleService roleService;
    private final SecurityDTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    

    public VendorService(IVendorRepository vendorRepository, VendorCodeGenerator codeGenerator, IRoleService roleService, SecurityDTOMapper dtoMapper, PasswordEncoder passwordEncoder) {
        this.vendorRepository = vendorRepository;
        this.codeGenerator = codeGenerator;
        this.roleService = roleService;
        this.dtoMapper = dtoMapper;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    @Transactional
    public VendorResponseDTO createVendor(VendorRequestDTO dto) {
        if (!dto.password().equals(dto.confirmPassword())) throw new BusinessRuleException("Las contraseñas no coinciden");

        String encodedPassword = passwordEncoder.encode(dto.password());
        Vendor newVendor = new Vendor(dto.name(), dto.lastName(), dto.dni(), encodedPassword);

        Vendor createdVendor = vendorRepository.save(newVendor);

        createdVendor.setCode(codeGenerator.vendorCodeGenertator(createdVendor.getId()));
        VendorRole role = new VendorRole(createdVendor, roleService.findRole(1L));

        createdVendor.addRole(role);

        return dtoMapper.vendorToDTO(createdVendor);
    }

    @Override
    public VendorResponseDTO getVendor(String code) {
        Vendor vendor = vendorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("El vendedor con el código " + code + " no se encuentra en nuestros registros"));
        return dtoMapper.vendorToDTO(vendor);
    }

    @Override
    public List<VendorResponseDTO> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream().map(vendor -> dtoMapper.vendorToDTO(vendor)).toList();
    }

    @Override
    @Transactional
    public VendorResponseDTO updateVendor(String code, VendorRequestDTO dto) {
        Vendor vendor = vendorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("El vendedor con el código " + code + " no se encuentra en nuestros registros"));
        if (!dto.password().equals(dto.confirmPassword())) throw new BusinessRuleException("Las contraseñas no coinciden");

        vendor.setName(dto.name());
        vendor.setLastName(dto.lastName());
        vendor.setDni(dto.dni());
        vendor.setPassword(passwordEncoder.encode(dto.password()));

        return dtoMapper.vendorToDTO(vendor);
    }

    @Override
    @Transactional
    public void deleteVendor(String requestUserCode, String code) {
        if (requestUserCode.equals(code)) throw new BusinessRuleException("No es posible eliminar al propio vendedor");
        Vendor vendor = vendorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("El vendedor con el código " + code + " no se encuentra en nuestros registros"));
        vendorRepository.delete(vendor);
    }

    @Override
    @Transactional
    public VendorResponseDTO assignRole(String code, Long roleId) {
        Vendor vendor = vendorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("El vendedor con el código " + code + " no se encuentra en nuestros registros"));
        VendorRole role = new VendorRole(vendor, roleService.findRole(roleId));
        vendor.addRole(role);
        return dtoMapper.vendorToDTO(vendor);
    }

    @Override
    @Transactional
    public VendorResponseDTO removeRole(String code, Long roleId) {
        Vendor vendor = vendorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("El vendedor con el código " + code + " no se encuentra en nuestros registros"));
        Role role = roleService.findRole(roleId);

        VendorRole roleToDelete = vendor.getRoles().stream().filter(vendorRole -> vendorRole.getRole().getName().equals(role.getName())).findFirst()
            .orElseThrow(() -> new BusinessRuleException("El vendedor no posee el rol indicado"));

        vendor.removeRole(roleToDelete);
        return dtoMapper.vendorToDTO(vendor);
    }



    @Override
    @Transactional
    public VendorResponseDTO changePassword(String code, VendorChangePasswordDTO dto) {
        Vendor vendor = vendorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("El vendedor con el código " + code + " no se encuentra en nuestros registros"));

        if (!dto.password().equals(dto.confirmPassword())) throw new BusinessRuleException("Las contraseñas no coinciden");

        String encodedPassword = passwordEncoder.encode(dto.password());
        vendor.setPassword(encodedPassword);
        
        return dtoMapper.vendorToDTO(vendor);
    }

}
