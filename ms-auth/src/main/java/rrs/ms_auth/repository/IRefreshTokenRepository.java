package rrs.ms_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rrs.ms_auth.model.RefreshToken;
import rrs.ms_auth.model.Vendor;


public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    Optional<RefreshToken> findByToken(String encryptedToken);

    void deleteByVendor(Vendor vendor);

}
