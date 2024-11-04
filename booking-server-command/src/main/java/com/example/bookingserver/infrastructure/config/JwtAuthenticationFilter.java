package com.example.bookingserver.infrastructure.config;

import com.example.bookingserver.application.command.service.JwtService;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.response.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    final RedisRepository redisRepository;
    private final UserDetailsService userDetailsService;
    final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String username;


        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response); // kiểm tra bước tiếp theo, vì có thể API không cần Jwt
            return;
        }

        jwt = authHeader.substring(7);
        Object a = redisRepository.get(jwt);
        if(a != null) { // kiểm tra JWT có đăng đang đăng xuất không
            return;
        }

        try {
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse apiResponse= ApiResponse.error(401, "Phiên đã hết hạn, vui lòng đăng nhập lại !");
            String content= objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(content);
        }
    }
}

