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

    @Query(value = """
        SELECT
            m.group_id,
            COALESCE(m.sender_id, (
                SELECT m2.sender_id
                FROM messages m2
                WHERE m2.group_id = m.group_id
                  AND m2.sender_id IS NOT NULL
                  AND m2.timestamp < m.timestamp
                ORDER BY m2.timestamp DESC
                LIMIT 1
            )) AS resolved_sender_id,
            m.timestamp
        FROM messages m
        JOIN (
            SELECT group_id, MAX(timestamp) AS latest_time
            FROM messages
            GROUP BY group_id
        ) latest
        ON m.group_id = latest.group_id AND m.timestamp = latest.latest_time
        ORDER BY m.timestamp DESC
        """, nativeQuery = true)
    List<Object[]> findLatestSenderPerGroup();

}
