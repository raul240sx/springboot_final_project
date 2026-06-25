package com.store.sales_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.sales_api.model.Client;


public interface IClientRepository extends JpaRepository<Client, Long>{

    Optional<Client> findByCode(String code);
}
