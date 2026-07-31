package rrs.ms_auth.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rrs.ms_auth.model.CustomVendorDetails;
import rrs.ms_auth.model.Vendor;
import rrs.ms_auth.repository.IVendorRepository;




@Service
public class CustomVendorDetailService implements UserDetailsService {
    private final IVendorRepository vendorRepository;


    public CustomVendorDetailService(IVendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Vendor user = vendorRepository.findByCode(username).orElseThrow(() -> new UsernameNotFoundException("Usuario con el código " + username + "no se encuentra en la base de datos"));
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(vendorRole -> new SimpleGrantedAuthority(vendorRole.getRole().getName())).toList();
        CustomVendorDetails userDetails = new CustomVendorDetails(user.getId(), user.getCode(), user.getPassword(), authorities);
        return userDetails;
    }

}
