package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotJpaRepository extends JpaRepository<Message, Long> {

    boolean existsBySenderId(String senderId);

    @Query("SELECT COALESCE(MAX(m.groupId), 0) FROM Message m")
    int findMaxGroupId();

    List<Message> findBySenderId(String senderId);

    List<Message> findByGroupId(int groupId);

    List<Message> findByGroupIdOrderByTimestampAsc(int groupId);

}
