package com.example.bookingserverquery.application.handler.exception;


import com.example.bookingserverquery.application.reponse.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Hệ thống đang bảo trì !"));
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiResponse> timeoutException(DataAccessResourceFailureException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi kết nối !"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<document.response.ApiResponse> exception(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(document.response.ApiResponse.error("Phương thức '" + e.getMethod() + "' không được hỗ trợ cho truy vấn này"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<document.response.ApiResponse> exception(NoHandlerFoundException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(document.response.ApiResponse.error(404, "Không tồn tại đường dẫn này"));
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
        return ResponseEntity.badRequest().body(ApiResponse.error(errorDetail.getCode() ,errorDetail.getMessage()));
    }
}
