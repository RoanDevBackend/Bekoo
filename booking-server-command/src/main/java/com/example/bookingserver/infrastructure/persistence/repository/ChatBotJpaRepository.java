package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatBotJpaRepository extends JpaRepository<Message, Long> {
    // Cần custom lại query
    boolean existsBySenderId(Long id);
    // Lấy groupIdMaximum

}
