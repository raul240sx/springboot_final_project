package com.store.sales_api.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    Optional<RefreshToken> findByToken(String encryptedToken);

    void deleteByVendor(Vendor vendor);

}
