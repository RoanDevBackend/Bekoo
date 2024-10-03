package com.example.bookingserver.application.handle;

import com.example.bookingserver.application.handle.exception.BookingCareException;

public interface Handler <T>{
    void execute(T command);
}
