package com.example.bookingserver.application.command.handle.exception;


import document.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi kết nối"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> exception(RuntimeException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> exception(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Phương thức '" + e.getMethod() + "' không được hỗ trợ cho truy vấn này"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> exception(NoHandlerFoundException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(404, "Không tồn tại đường dẫn này"));
    }

    /**
     * Xử lí lỗi không quá 10MB dữ liệu đối với file
     */

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Kích thước file không quá 10 MB"));
    }

    /**
     * Xử lí lỗi, chuyển đổi kiểu dữ liệu thời gian
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> exception(DateTimeParseException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Định dạng dữ liệu thời gian chưa chính xác"));
    }

    /**
     * Bắt lỗi thiếu tham số truyền vào qua request param
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex) {
        String errorMessage = "Tham số '" + ex.getParameterName() + "' là bắt buộc!";
        return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentException(IllegalArgumentException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    /**
     * Lỗi kết nối đến database hay là bên thứ 3
     */
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiResponse> timeoutException(DataAccessResourceFailureException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi kết nối !"));
    }

    /**
     * Xử lí validate dữ liệu đầu vào của Body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgument(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()));
    }


    /**
     * Xử lí lỗi của hệ thống tự định nghĩa ra
     */
    @ExceptionHandler(BookingCareException.class)
    public ResponseEntity<ApiResponse> bookingCareAppException(BookingCareException e){
        ErrorDetail errorDetail= e.getErrorDetail();
        log.error(errorDetail.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(errorDetail.getCode(), errorDetail.getMessage()));
    }
}
