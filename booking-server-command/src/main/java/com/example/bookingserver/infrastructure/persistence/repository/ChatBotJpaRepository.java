package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBotJpaRepository extends JpaRepository<Message, Long> {

    boolean existsBySenderId(String senderId);

    @Query("SELECT COALESCE(MAX(m.groupId), 0) FROM Message m")
    int findMaxGroupId();

    Message findBySenderId(String senderId);
}
