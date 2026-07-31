package rrs.ms_clients.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rrs.ms_clients.model.Client;


public interface IClientRepository extends JpaRepository<Client, Long>{

    Optional<Client> findByCode(String code);
}
