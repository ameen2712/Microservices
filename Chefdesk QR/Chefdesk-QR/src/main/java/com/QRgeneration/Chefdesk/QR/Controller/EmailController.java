package com.QRgeneration.Chefdesk.QR.Controller;

import com.QRgeneration.Chefdesk.QR.Service.EmailFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailFetcherService emailFetcherService;

    @Autowired
    public EmailController(EmailFetcherService emailFetcherService) {
        this.emailFetcherService = emailFetcherService;
    }

    @GetMapping("/fetch")
    public String fetchEmails() {
        emailFetcherService.fetchAndStoreEmails();
        return "Email fetching process started!";
    }
}