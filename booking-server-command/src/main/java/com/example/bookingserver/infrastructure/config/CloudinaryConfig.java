package com.example.bookingserver.infrastructure.config;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    String CLOUD_NAME;
    @Value("${cloudinary.api_key}")
    String API_KEY;
    @Value("${cloudinary.api_secret}")
    String API_SECRET;

    @Bean
    public Cloudinary cloudinary(){
        Map<Object , Object> map = new HashMap<>() ; // == Map map = new HashMap()
        map.put("cloud_name" , "dytxoysey") ;
        map.put("api_key" ,"921877442766949") ;
        map.put("api_secret" , "CQK7pbQY7BwcxgNGwf6kzujrEjY") ;
        map.put("secure" , true) ;
        return new Cloudinary(map) ;
    }
}