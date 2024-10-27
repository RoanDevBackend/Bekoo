package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;

@Repository
public interface DoctorELRepository extends ElasticsearchRepository<Doctor, String> {
    @Query("{\"nested\": {\"path\": \"user\", \"query\": {\"match\": {\"user.name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}}}}")
    Page<Doctor> findDoctorsByName(String name, Pageable pageable);

}
