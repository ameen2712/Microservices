package com.QRgeneration.Chefdesk.QR.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailgunResponse2 {

    @JsonProperty("items")
    private List<Item> items;

    @JsonProperty("paging")
    private Paging paging;  // Added paging field for next/previous URLs

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String recipient;
        private String reason;
        private double timestamp;

        @JsonProperty("delivery-status")
        private DeliveryStatus deliveryStatus;

        private Message message;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryStatus {
        private String message;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private Map<String, String> headers;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Paging {
        private String previous;
        private String next;
        private String first;
        private String last;
    }
}
