package com.example.bookingserver.application.command.handle;

public interface HandlerDTO<T, U> {

    U execute(T command);
}
