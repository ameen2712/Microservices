package com.QRgeneration.Chefdesk.QR.Service;


import com.QRgeneration.Chefdesk.QR.DTO.MailgunResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Service
public class EmailFetcherService {

    private static final String API_URL = "https://api.mailgun.net/v3/lists/visitors@cineroyal.info/members/pages?=first&limit=1000";
    private static final String API_KEY = "key-3m4snx9mzyb4pvvf4mwea6vsqfkg51-8";

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(EmailFetcherService.class);

    @Autowired
    public EmailFetcherService(JdbcTemplate jdbcTemplate, RestTemplate restTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    public void fetchAndStoreEmails() {
        String nextUrl = API_URL;

        while (nextUrl != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setBasicAuth("api", API_KEY);
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<MailgunResponse> response = restTemplate.exchange(nextUrl, HttpMethod.GET, entity, MailgunResponse.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    MailgunResponse mailgunResponse = response.getBody();

                    for (MailgunResponse.Member member : mailgunResponse.getItems()) {
                        String email = member.getAddress();
                        if (email != null && !email.isEmpty()) {
                            insertEmail(email);
                        }
                    }

                    nextUrl = mailgunResponse.getPaging().get("next");
                } else {
                    logger.warn("Unexpected response: {}", response.getStatusCode());
                    break;
                }
            } catch (Exception e) {
                logger.error("Error fetching emails: {}", e.getMessage());
                break;
            }
        }
    }

    private void insertEmail(String email) {
        try {
            Date date = new Date();
            String sql = "INSERT INTO users (version,mobile,name, email,to_date,from_date,mailing_list_name,active) VALUES (?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, 1,null,null, email, null, date,"visitors@cineroyal.info",true);
            logger.info("Inserted email: {}", email);
        } catch (Exception e) {
            logger.error("Error inserting email: {}", e.getMessage());
        }
    }
}