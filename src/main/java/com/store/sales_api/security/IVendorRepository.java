package com.store.sales_api.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IVendorRepository extends JpaRepository<Vendor, Long>{

    @EntityGraph(attributePaths = {"roles"})
    public Optional<Vendor> findByCode(String code);

}
