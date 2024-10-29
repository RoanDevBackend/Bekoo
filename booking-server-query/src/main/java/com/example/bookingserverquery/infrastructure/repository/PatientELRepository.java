package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Patient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientELRepository extends ElasticsearchRepository<Patient, String> {
}
