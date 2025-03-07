package com.QRgeneration.Chefdesk.QR.Repository;
import com.QRgeneration.Chefdesk.QR.models.FailedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedEmailRepository extends JpaRepository<FailedEmail, Long> {
}