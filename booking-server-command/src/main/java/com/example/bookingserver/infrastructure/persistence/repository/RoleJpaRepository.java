package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, String> {
}
