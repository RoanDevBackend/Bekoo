package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.EmergencyContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyContactELRepository extends ElasticsearchRepository<EmergencyContact, Long> {
    @Query(value = "{\n" +
            "    \"nested\": {\n" +
            "      \"path\": \"patient\",\n" +
            "      \"query\": {\n" +
            "        \"term\": {\n" +
            "          \"patient.id\": {\n" +
            "            \"value\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }")
    Page<EmergencyContact> findAllByPatientId(String patientId, Pageable pageable);
}
