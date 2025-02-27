package com.QRgeneration.Chefdesk.QR.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "secUser")
public class SecUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long version;

    @Column(nullable = false)
    private boolean passwordExpired;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private boolean accountLocked;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean accountExpired;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Column(nullable = false)
    private boolean enabled;

    @Column
    private String email;

    @Column
    private String lastname;

    @Column(name = "first_name")
    private String firstName;

    @Column
    private String mobile;

    @Column
    private String reset;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    private boolean bioMetricEnabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column
    private String mobileOtp;

    @Column(nullable = false)
    private boolean isMobileOtpVerified;

    @Column
    private String emailOtp;

    @Column(nullable = false)
    private boolean isEmailOtpVerified;

    @Column(nullable = false, columnDefinition = "int default 5")
    private int availableRetries;
}