package com.example.bookingserver;

import com.example.bookingserver.application.command.command.user.CreateUserCommand;
import com.example.bookingserver.application.command.handle.user.CreateUserHandler;
import com.example.bookingserver.domain.ERole;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.infrastructure.persistence.repository.RoleJpaRepository;
import com.example.bookingserver.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class BookingServerCommandApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookingServerCommandApplication.class, args);
	}

	final RoleJpaRepository roleJpaRepository;
	final UserJpaRepository userJpaRepository;
	final CreateUserHandler createUserHandler;
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
		if(!userJpaRepository.existsByEmail("bekoo.admin@gmail.com")){
			CreateUserCommand command = new CreateUserCommand();
			command.setName("Quản trị hệ thống");
			command.setCccd("036204018701");
			command.setEmail("bekoo.admin@gmail.com");
			command.setPassword("Admin@1234");
			command.setConfirmPassword("Admin@1234");
			command.setPhoneNumber("0379715226");
			command.setProvince("Nam Định");
			command.setDistrict("Giao Thuỷ");
			command.setCommune("Giao Long");
			command.setDob("2004-12-24");
			command.setGender("Nam");
			Set<Role> roles = new HashSet<>();
			roles.add(new Role(ERole.ADMIN));
			roles.add(new Role(ERole.USER));
			createUserHandler.execute(command, roles);
			log.info("Create ADMIN account successfully");
		}
	}
}
