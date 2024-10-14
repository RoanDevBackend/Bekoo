package com.example.bookingserver.application.handle;

public interface Handler_DTO<T, U> {

    U execute(T command);
}
