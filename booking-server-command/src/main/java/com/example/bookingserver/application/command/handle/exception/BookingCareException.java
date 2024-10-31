package com.example.bookingserver.application.command.handle.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCareException extends RuntimeException{
    private ErrorDetail errorDetail;
}
