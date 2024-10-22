package com.example.bookingserver.application.command.handle;

public interface Handler <T>{
    void execute(T command);
}
