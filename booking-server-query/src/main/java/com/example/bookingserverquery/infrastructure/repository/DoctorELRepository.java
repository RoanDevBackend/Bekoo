package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorELRepository extends ElasticsearchRepository<Doctor, String> {
    @Query("""
        {
          "nested": {
            "path": "user",
            "query": {
              "match": {
                "user.name": {
                  "query": "?0",
                  "fuzziness": "AUTO"
                }
              }
            }
          }
        }
        """)
    Page<Doctor> findDoctorsByName(String name, Pageable pageable);

    @Query("{\n" +
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
    Optional<Doctor> findDoctorByUserId(String userId);
}
