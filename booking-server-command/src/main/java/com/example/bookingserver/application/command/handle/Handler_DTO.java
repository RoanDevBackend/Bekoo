package com.example.bookingserver.application.command.handle;

public interface Handler_DTO<T, U> {

    U execute(T command);
}
