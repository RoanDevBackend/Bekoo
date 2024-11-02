package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Schedule;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleELRepository extends ElasticsearchRepository<Schedule, String> {

    @Query(value = "{\n" +
            "    \"nested\": {\n" +
            "      \"path\": \"doctor\",\n" +
            "      \"query\": {\n" +
            "        \"term\": {\n" +
            "          \"doctor.id\": {\n" +
            "            \"value\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }", count = true)
    Long countByDoctorId(String doctorId);

    @Query(value = "{\n" +
            "    \"nested\": {\n" +
            "      \"path\": \"specialize\",\n" +
            "      \"query\": {\n" +
            "        \"term\": {\n" +
            "          \"specialize.id.keyword\": {\n" +
            "            \"value\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }", count = true)
    Long countBySpecializedId(String specializedId);

}
