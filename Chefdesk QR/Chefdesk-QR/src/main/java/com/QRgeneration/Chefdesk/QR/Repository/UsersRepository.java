package com.QRgeneration.Chefdesk.QR.Repository;

import com.QRgeneration.Chefdesk.QR.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String username);
}
