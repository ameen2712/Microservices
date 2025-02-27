package com.QRgeneration.Chefdesk.QR.Repository;


import com.QRgeneration.Chefdesk.QR.models.SecUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecUserRepository extends JpaRepository<SecUser, Long> {
    boolean existsByUsername(String username);
}