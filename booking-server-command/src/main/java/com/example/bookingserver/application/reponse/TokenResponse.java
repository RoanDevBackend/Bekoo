package com.example.bookingserver.application.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {
    String tokenContent ;
    String refreshToken ;
    String userId;
    String userName ;
    Set<String> roleName ;
    Timestamp expToken ;
    Timestamp expRefreshToken;
}