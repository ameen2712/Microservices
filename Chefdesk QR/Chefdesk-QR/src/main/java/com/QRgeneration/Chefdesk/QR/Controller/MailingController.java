package com.QRgeneration.Chefdesk.QR.Controller;


import com.QRgeneration.Chefdesk.QR.Service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mailing")
public class MailingController {

    @Autowired
    private final MailingService mailingService;

    public MailingController(MailingService mailingService) {
        this.mailingService = mailingService;
    }


    @PostMapping("/process")
    public String processEmails() {
        mailingService.processUnmatchedEmails();
        return "Processing completed!";
    }
}
