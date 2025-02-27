package com.QRgeneration.Chefdesk.QR.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MailgunResponse {
    private List<Member> items;
    private Map<String, String> paging;

    @Getter
    @Setter
    public static class Member {
        private String address;
    }
}