package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user){
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userJpaRepository.findUserByPhoneNumber(phoneNumber);
    }


    @Override
    public Optional<User> signIn(String phoneNumber, String password) {

        return userJpaRepository.findUserByPhoneNumberAndPassword(phoneNumber, password);
    }

    @Override
    public void delete(String id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean isPhoneNumberExisted(String phoneNumber) {
       return userJpaRepository.existsByPhoneNumber(phoneNumber);
    }
}
