package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

//    @Query(" {\n" +
//            "    \"bool\": {\n" +
//            "      \"should\": [\n" +
//            "        {\n" +
//            "          \"nested\": {\n" +
//            "            \"path\": \"doctor\",\n" +
//            "            \"query\": {\n" +
//            "              \"term\": {\n" +
//            "                \"doctor.id\": {\n" +
//            "                  \"value\": \"?0\"\n" +
//            "                }\n" +
//            "              }\n" +
//            "            }\n" +
//            "          }\n" +
//            "        },\n" +
//            "        {\n" +
//            "          \"nested\": {\n" +
//            "            \"path\": \"patient\",\n" +
//            "            \"query\": {\n" +
//            "              \"term\": {\n" +
//            "                \"patient.id\": {\n" +
//            "                  \"value\": \"?1\"\n" +
//            "                }\n" +
//            "              }\n" +
//            "            }\n" +
//            "          }\n" +
//            "        }\n" +
//            "      ]\n" +
//            "    }\n" +
//            "  }")
//    Page<Schedule> findScheduleByDoctorIdOrPatientId(String doctorId, String patientId);

    @Query("{\n" +
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
    Page<Schedule> findByPatientId(String patientId, Pageable pageable);

    @Query("{\n" +
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
            "  }")
    Page<Schedule> findByDoctorId(String doctorId, Pageable pageable);
}
