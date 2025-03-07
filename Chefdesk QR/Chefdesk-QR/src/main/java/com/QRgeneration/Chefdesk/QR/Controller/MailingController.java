package com.QRgeneration.Chefdesk.QR.Controller;

import com.QRgeneration.Chefdesk.QR.Service.EmailFetcherService2;
import com.QRgeneration.Chefdesk.QR.Service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mailing")
public class MailingController {

    private final MailingService mailingService;
    private final EmailFetcherService2 emailFetcherService;

    @Autowired
    public MailingController(MailingService mailingService, EmailFetcherService2 emailFetcherService) {
        this.mailingService = mailingService;
        this.emailFetcherService = emailFetcherService;
    }

    @PostMapping("/process")
    public String processEmails() {
        mailingService.processUnmatchedEmails();
        return "Processing completed!";
    }

    @GetMapping("/fetch-failed")
    public String fetchFailedEmails() {
        emailFetcherService.fetchAndStoreFailedEmails();
        return "Fetching failed emails initiated.";
    }
}
