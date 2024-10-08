package com.example.bookingserverquery.domain.repository;

import com.example.bookingserverquery.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Page<User> findByName(String name, Pageable pageable);
    Page<User> getAll(Pageable pageable);
    Optional<User> findById(String id);
}
