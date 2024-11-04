package com.example.bookingserverquery.application.service.i;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

//    String generateToken(UserDetails userDetails, Long time);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Long time);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);
//    Long getExpirationToken(String token);
}
