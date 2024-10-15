package com.example.bookingserverquery.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${spring.url}")
    private String springUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bekoo")
                        .version("1.0")
                        .description("RestApi for front-end developer")
                        .contact(new Contact()
                                .name("Roãn Văn Quyền")
                                .email("roan.dev.backend@gmail.com"))
                )
                .addServersItem(new Server()
                        .url(springUrl)
                        .description("Query"))
                ;
    }

}
