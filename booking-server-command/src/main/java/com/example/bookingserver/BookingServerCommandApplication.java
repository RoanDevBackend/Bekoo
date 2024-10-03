package com.example.bookingserver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info =  @Info(
				title = "Bekoo" ,
				version = "1.0" ,
				description = "RestApi for front-end developer" ,
				contact = @Contact(
						name = "Roãn Văn Quyền"
						, email = "roan.dev.backend@gmail.com"
				)
		),
		servers =@Server(
				url = "http://localhost:8080",
				description = "Bekoo"
		)
)
public class BookingServerCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServerCommandApplication.class, args);
	}

}
