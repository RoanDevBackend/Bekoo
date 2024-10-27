package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Department;
import com.example.bookingserverquery.domain.Specialize;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializeELRepository extends ElasticsearchRepository<Specialize, String> {
    Integer countByDepartment(Department department);
}
