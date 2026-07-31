package rrs.ms_auth.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rrs.ms_auth.model.Role;
import rrs.ms_auth.model.Vendor;
import rrs.ms_auth.model.VendorRole;
import rrs.ms_auth.repository.IRoleRepository;
import rrs.ms_auth.repository.IVendorRepository;


@Component
public class DataInitializer implements CommandLineRunner{
    private final IVendorRepository vendorRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initializer.first-user-code}")
    private String code;

    @Value("${app.initializer.first-user-name}")
    private String name;

    @Value("${app.initializer.first-user-last-name}")
    private String lastName;

    @Value("${app.initializer.first-user-dni}")
    private String dni;

    @Value("${app.initializer.first-user-password}")
    private String password;


    public DataInitializer(IVendorRepository vendorRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.vendorRepository = vendorRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (vendorRepository.count() == 0  && roleRepository.count() == 0) {

            //Crear Rol ADMIN y VENDOR
            Role role1 = new Role("ROLE_ADMIN");
            Role role2 = new Role("ROLE_VENDOR");
            List<Role> createdRoles = roleRepository.saveAll(List.of(role1, role2));


            //Crear primer usuario
            String encodedPassword = passwordEncoder.encode(this.password);
            Vendor firstVendor = new Vendor(this.name, this.lastName, this.dni, encodedPassword);
            firstVendor.setCode(this.code);
            firstVendor.addRole(new VendorRole(firstVendor, createdRoles.get(0)));
            firstVendor.addRole(new VendorRole(firstVendor, createdRoles.get(1)));
            vendorRepository.save(firstVendor);
        }
    }

}
