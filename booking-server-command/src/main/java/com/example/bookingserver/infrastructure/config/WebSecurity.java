package com.example.bookingserver.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurity {

    final AuthenticationProvider authenticationProvider;
    final JwtAuthenticationFilter jwtAuthenticationFilter;

    final String[] listUnAuthenticate={
            "/sign-in"
            , "/user/forgot-password/send-otp"
            , "/user/forgot-password/verify"
            , "/user/password/otp"
            , "/v3/api-docs/**"
            , "/swagger-ui/**"
            , "/swagger-ui.html"
            , "specialize/department/**"
            , "/payment/result/**"
            , "/api/**"
    };

    final String[] getMappings={
            "/specialize/**"
    };
    final String[] postMappings={
            "/token/refresh/**"
            ,"/specialize/query-all"
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> corsFilter())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        configure ->
                                configure
                                        .requestMatchers(HttpMethod.POST, "/user")
                                            .permitAll()
                                        .requestMatchers(listUnAuthenticate)
                                            .permitAll()
                                        .requestMatchers(HttpMethod.GET ,getMappings)
                                            .permitAll()
                                        .requestMatchers(HttpMethod.POST ,postMappings)
                                            .permitAll()
                                        .requestMatchers(HttpMethod.POST, "patient/**")
                                        .hasAuthority("USER")
                                        .anyRequest()
                                            .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration() ;
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
        source.registerCorsConfiguration("/**" , corsConfiguration);
        return new CorsFilter(source) ;
    }
}
