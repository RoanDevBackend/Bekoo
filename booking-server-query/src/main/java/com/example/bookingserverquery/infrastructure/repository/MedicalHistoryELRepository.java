package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.MedicalHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalHistoryELRepository extends ElasticsearchRepository<MedicalHistory, Long> {
}
