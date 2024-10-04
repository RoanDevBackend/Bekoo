package com.example.bookingserver.application.handle.exception;


import com.example.bookingserver.application.reponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse illegalArgumentException(IllegalArgumentException e){
        log.error(e.getMessage());
        return ApiResponse.error(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse methodArgument(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        return ApiResponse.error(e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(BookingCareException.class)
    public ApiResponse bookingCareAppException(BookingCareException e){
        ErrorDetail errorDetail= e.getErrorDetail();
        log.error(errorDetail.getMassage());
        return ApiResponse.error(errorDetail.getMassage());
    }
}
