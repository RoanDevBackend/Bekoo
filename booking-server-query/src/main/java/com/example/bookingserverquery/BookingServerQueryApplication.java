package com.example.bookingserverquery;


import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@SpringBootApplication
@OpenAPIDefinition(
        info =  @Info(
                title = "Bekoo-Query" ,
                version = "1.0" ,
                description = "RestApi for front-end developer" ,
                contact = @Contact(
                        name = "Roãn Văn Quyền"
                        , email = "roan.dev.backend@gmail.com"
                )
        ),
        servers =@Server(
                url = "http://localhost:8081",
                description = "Bekoo-Query"
        )
)
public class BookingServerQueryApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(BookingServerQueryApplication.class, args);
    }

    @Autowired
    UserELRepository userELRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
