package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Patient;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientELRepository extends ElasticsearchRepository<Patient, String> {
    @Query(value = "{\n" +
            "    \"nested\": {\n" +
            "      \"path\": \"user\",\n" +
            "      \"query\": {\n" +
            "        \"term\": {\n" +
            "          \"user.id.keyword\": {\n" +
            "            \"value\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }")
    Optional<Patient> findByUserId(String userId);
}
