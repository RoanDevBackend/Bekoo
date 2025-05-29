package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<Message, Long> {
}
