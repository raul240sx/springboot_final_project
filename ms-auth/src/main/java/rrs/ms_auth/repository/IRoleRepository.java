package rrs.ms_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rrs.ms_auth.model.Role;


public interface IRoleRepository extends JpaRepository<Role, Long>{

}
