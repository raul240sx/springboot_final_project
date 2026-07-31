package rrs.ms_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import rrs.ms_auth.model.Vendor;


public interface IVendorRepository extends JpaRepository<Vendor, Long>{

    @EntityGraph(attributePaths = {"roles"})
    public Optional<Vendor> findByCode(String code);

}