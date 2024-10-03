package com.example.bookingserver.application.handle.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCareException extends Exception{
    private ErrorDetail errorDetail;
}
