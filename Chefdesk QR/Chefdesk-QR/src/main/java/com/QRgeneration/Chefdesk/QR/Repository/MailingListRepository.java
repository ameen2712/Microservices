package com.QRgeneration.Chefdesk.QR.Repository;

import com.QRgeneration.Chefdesk.QR.models.mailingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailingListRepository extends JpaRepository<mailingList, Long> {
}