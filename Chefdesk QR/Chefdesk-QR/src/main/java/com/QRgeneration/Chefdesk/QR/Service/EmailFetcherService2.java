package com.QRgeneration.Chefdesk.QR.Service;

import com.QRgeneration.Chefdesk.QR.DTO.MailgunResponse2;
import com.QRgeneration.Chefdesk.QR.Repository.FailedEmailRepository;
import com.QRgeneration.Chefdesk.QR.models.FailedEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailFetcherService2 {

    private static final String API_URL = "https://api.mailgun.net/v3/cineroyal.info/events?event=failed";
    private static final String API_KEY = "key-3m4snx9mzyb4pvvf4mwea6vsqfkg51-8";

    private final RestTemplate restTemplate;
    private final FailedEmailRepository failedEmailRepository;

    @Autowired
    public EmailFetcherService2(FailedEmailRepository failedEmailRepository, RestTemplate restTemplate) {
        this.failedEmailRepository = failedEmailRepository;
        this.restTemplate = restTemplate;
    }

    public void fetchAndStoreFailedEmails() {
        try {
            String nextUrl = API_URL; // Start with the initial API URL

            while (nextUrl != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setBasicAuth("api", API_KEY);
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<MailgunResponse2> response = restTemplate.exchange(nextUrl, HttpMethod.GET, entity, MailgunResponse2.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    MailgunResponse2 mailgunResponse = response.getBody();

                    for (MailgunResponse2.Item item : mailgunResponse.getItems()) {
                        if (item.getMessage() != null && item.getMessage().getHeaders() != null) {
                            String recipient = item.getRecipient();
                            String reason = item.getReason();
                            String errorMessage = item.getDeliveryStatus() != null ? item.getDeliveryStatus().getMessage() : "";
                            long timestamp = (long) item.getTimestamp();

                            if (recipient != null && !recipient.isEmpty()) {
                                saveFailedEmail(recipient, reason, errorMessage, timestamp);
                            }
                        }
                    }

                    // Extract the next URL from the response
                    nextUrl = (mailgunResponse.getPaging() != null) ? mailgunResponse.getPaging().getNext() : null;

                    // Print the next URL for debugging
                    System.out.println("Next URL: " + nextUrl);

                } else {
                    System.out.println("Unexpected response: " + response.getStatusCode());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching failed emails: " + e.getMessage());
        }
    }

    private void saveFailedEmail(String recipient, String reason, String errorMessage, long timestamp) {
        try {
            FailedEmail email = new FailedEmail();
            email.setRecipient(recipient);
            email.setReason(reason);
            email.setErrorMessage(errorMessage);
            email.setTimestamp(timestamp);

            failedEmailRepository.save(email);
            System.out.println("Saved failed email: " + recipient);
        } catch (Exception e) {
            System.err.println("Error saving failed email: " + e.getMessage());
        }
    }
}
