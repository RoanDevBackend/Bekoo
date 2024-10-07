package com.example.bookingserver;

import com.example.bookingserver.domain.ERole;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.infrastructure.persistence.repository.RoleJpaRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class BookingServerCommandApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookingServerCommandApplication.class, args);
	}



	@Autowired
	RoleJpaRepository roleJpaRepository;

	@Override
	public void run(ApplicationArguments args){
		if(!roleJpaRepository.existsById(ERole.ADMIN.name())) {
			Role role_admin = new Role(ERole.ADMIN);
			roleJpaRepository.save(role_admin);
		}
		if(!roleJpaRepository.existsById(ERole.DOCTOR.name())) {
			Role role = new Role(ERole.DOCTOR);
			roleJpaRepository.save(role);
		}
		if(!roleJpaRepository.existsById(ERole.USER.name())) {
			Role role = new Role(ERole.USER);
			roleJpaRepository.save(role);
		}
	}
}
