package com.example.bookingserver.infrastructure.constant;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public class ApplicationConstant {
    public static class EventStatus {
        public static String PENDING = "pending";
        public static String SEND = "send";
        public static String FAILED = "failed";
    }
}
