package com.example.bookingserver.application.service.impl;

import com.example.bookingserver.application.reponse.SmsResponse;
import com.example.bookingserver.application.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.EventSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public SmsResponse send(String to, String content) {
        return null;
    }
}
