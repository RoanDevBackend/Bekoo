package com.example.bookingserverquery.infrastructure.repository.impl;

import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.domain.repository.UserRepository;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {


    final UserELRepository userELRepository;

    @Override
    public Page<User> findByName(String name, Pageable pageable) {
        return userELRepository.findUsersByName(name, pageable);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userELRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(String id) {
        return userELRepository.findById(id);
    }
}
