package com.QRgeneration.Chefdesk.QR.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long version;

    @Column
    private String mobile;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private Date to_date;

    @Column
    private Date from_date;

    @Column
    private String mailing_list_name;

    @Column
    private Boolean active;


}
