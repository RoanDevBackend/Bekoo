package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Specialize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializeELRepository extends ElasticsearchRepository<Specialize, String> {
    @Query(value = " {\n" +
            "    \"nested\": {\n" +
            "      \"path\": \"department\",\n" +
            "      \"query\": {\n" +
            "        \"term\": {\n" +
            "          \"department.id.keyword\": {\n" +
            "            \"value\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }" +
            "}", count = true)
    Long countByDepartment(String departmentId);

    @Query(value = " {\n" +
            "    \"nested\": {\n" +
            "      \"path\": \"department\",\n" +
            "      \"query\": {\n" +
            "        \"term\": {\n" +
            "          \"department.id.keyword\": {\n" +
            "            \"value\": \"?0\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }" +
            "}")
    Page<Specialize> findAllByDepartment(String departmentId, Pageable pageable);
}
