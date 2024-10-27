package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Schedule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleELRepository extends ElasticsearchRepository<Schedule, String> {
}
