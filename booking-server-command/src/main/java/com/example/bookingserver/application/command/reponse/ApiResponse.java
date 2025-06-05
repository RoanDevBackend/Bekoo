package com.example.bookingserver.application.command.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    int code;
    String message;
    Object value;
    HttpStatus status;

//    HttpHeaders headers;

//    public ApiResponse(int code, String message, Object value, HttpStatus status) {
//        this.code = code;
//        this.message = message;
//        this.value = value;
//        this.status = status;
//        HttpHeaders httpHeaders= new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        this.headers= httpHeaders;
//    }

    public static ApiResponse success(int code, String message){
        return new ApiResponse(code, message, null, HttpStatus.ACCEPTED);
    }

    public static ApiResponse success(int code, String message, HttpStatus status){
        return new ApiResponse(code, message, null, status);
    }

    public static ApiResponse success(int code, String message, Object value){
        return new ApiResponse(code, message, value, null);
    }

    public static ApiResponse success(int code, String message, Object value, HttpStatus status) {
        return new ApiResponse(code, message, value, status);
    }

    public static ApiResponse error(String message){
        return new ApiResponse(400, message, null, HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse error(int code, String message){
        org.springframework.http.HttpHeaders httpHeaders= new org.springframework.http.HttpHeaders();
        return new ApiResponse(code, message, null, HttpStatus.BAD_REQUEST);
    }


    public static ApiResponse error(int code, String message, Object value){
        return new ApiResponse(code, message, value, HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse error(String message, HttpStatus status){
        org.springframework.http.HttpHeaders httpHeaders= new org.springframework.http.HttpHeaders();
        return new ApiResponse(400, message, null, status);
    }

}
