package com.example.bookingserver.application.command.handle.exception;


import com.example.bookingserver.application.command.reponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentException(IllegalArgumentException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgument(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getFieldError().getDefaultMessage()));
    }


    @ExceptionHandler(BookingCareException.class)
    public ResponseEntity<ApiResponse> bookingCareAppException(BookingCareException e){
        ErrorDetail errorDetail= e.getErrorDetail();
        log.error(errorDetail.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(errorDetail.getCode(), errorDetail.getMessage()));
    }
}
