package com.QRgeneration.Chefdesk.QR.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "failed_emails")
public class FailedEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    private String reason;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(nullable = false)
    private Long timestamp;
}
