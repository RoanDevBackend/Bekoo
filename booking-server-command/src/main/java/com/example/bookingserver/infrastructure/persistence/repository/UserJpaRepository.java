package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String> {

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndPassword(String email, String password);
    @Query("SELECT COUNT (*) " +
            "FROM User u " +
            "WHERE u.dob >= :start and u.dob < :end")
    int countByDob(LocalDate start, LocalDate end);
}
