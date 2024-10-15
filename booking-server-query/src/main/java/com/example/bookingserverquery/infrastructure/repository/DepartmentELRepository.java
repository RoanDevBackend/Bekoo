package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentELRepository extends ElasticsearchRepository<Department, String> {
    @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    Page<Department> findDepartmentsByName(String name, Pageable pageable);
}
