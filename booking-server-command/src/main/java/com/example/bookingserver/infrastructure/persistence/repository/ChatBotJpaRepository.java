package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatBotJpaRepository extends JpaRepository<Message, Long> {

    boolean existsBySenderId(String senderId);

    @Query("SELECT COALESCE(MAX(m.groupId), 0) FROM Message m")
    int findMaxGroupId();

    List<Message> findBySenderId(String senderId);

    List<Message> findByGroupId(int groupId);

    @Query(value = "SELECT m.groupId " +
            "FROM Message m " +
            "WHERE m.sender.id = :senderId ")
    Integer findFirstBySenderId(String senderId);

    @Query("FROM Message m " +
            "WHERE ( (:isSenderNotNull = false OR m.sender IS NOT NULL) )" +
            "AND m.groupId = :groupId " +
            "ORDER BY m.id desc")
    Page<Message> getMessageByGroupId(int groupId, boolean isSenderNotNull, Pageable pageable);

    @Query("FROM Message m " +
            "    WHERE m.id IN (" +
            "        SELECT MAX(m2.id) " +
            "        FROM Message m2 " +
            "        GROUP BY m2.groupId " +
            "    ) " +
            "    ORDER BY m.id DESC")
    List<Message> getAllChat();
}
