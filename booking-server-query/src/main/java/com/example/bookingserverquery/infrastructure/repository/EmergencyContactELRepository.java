package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.EmergencyContact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmergencyContactELRepository extends ElasticsearchRepository<EmergencyContact, Long> {
}
