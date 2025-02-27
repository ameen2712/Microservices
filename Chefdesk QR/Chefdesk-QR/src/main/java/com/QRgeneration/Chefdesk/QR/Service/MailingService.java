package com.QRgeneration.Chefdesk.QR.Service;

import com.QRgeneration.Chefdesk.QR.Repository.MailingListRepository;
import com.QRgeneration.Chefdesk.QR.Repository.SecUserRepository;
import com.QRgeneration.Chefdesk.QR.Repository.UsersRepository;
import com.QRgeneration.Chefdesk.QR.models.SecUser;
import com.QRgeneration.Chefdesk.QR.models.Users;
import com.QRgeneration.Chefdesk.QR.models.mailingList;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class MailingService {

    private final UsersRepository usersRepository;
    private final SecUserRepository secUserRepository;
    private final MailingListRepository mailingListRepository;


    public MailingService(UsersRepository usersRepository, SecUserRepository secUserRepository, MailingListRepository mailingListRepository) {
        this.usersRepository = usersRepository;
        this.secUserRepository = secUserRepository;
        this.mailingListRepository = mailingListRepository;
    }

    public void processUnmatchedEmails() {
        List<SecUser> secUsersList = secUserRepository.findAll();
        long count =1;
        for (SecUser secuser : secUsersList) {
            String email = secuser.getEmail();

            if (!usersRepository.existsByEmail(email)) {
                mailingList mailingEntry = new mailingList();
                mailingEntry.setUnMatchedEmail(email);
                System.out.println("Unmatched email COUNT is "+count +" >>>>"+ email);
                mailingListRepository.save(mailingEntry);
                count++;
            }
        }
    }
}
