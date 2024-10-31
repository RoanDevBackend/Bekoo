package com.example.bookingserverquery;


import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.ScheduleELRepository;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@SpringBootApplication
public class BookingServerQueryApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(BookingServerQueryApplication.class, args);
    }




    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
