package com.store.sales_api.client;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IClientRepository extends JpaRepository<Client, Long>{

    Optional<Client> findByCode(String code);
}
