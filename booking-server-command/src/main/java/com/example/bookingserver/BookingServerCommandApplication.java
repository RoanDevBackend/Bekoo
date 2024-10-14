package com.example.bookingserver;

import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.ERole;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.infrastructure.persistence.repository.DoctorJpaRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RoleJpaRepository;
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

@SpringBootApplication
public class BookingServerCommandApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookingServerCommandApplication.class, args);
	}



	@Autowired
	RoleJpaRepository roleJpaRepository;
	@Autowired
	DoctorRepository doctorRepository;

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
