package com.example.bookingserver.infrastructure.constant;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationConstant {
    public static class EventStatus {
        public static String PENDING = "pending";
        public static String SEND = "send";
        public static String FAILED = "failed";
    }
    public static class Status{
        public static int CONFIRMED = 1;
        public static int CANCELLED = 2;
        public static int OK = 3;
        public static int EXPIRED = 4;
    }
    public static class EventType{
        public static String ADD= "add";
        public static String UPDATE= "update";
        public static String DELETE= "delete";
    }
}
