package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.domain.DoctorDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDepartmentELRepository extends ElasticsearchRepository<DoctorDepartment, String> {
    Page<DoctorDepartment> findByDepartmentId(String id, Pageable pageable);
    void deleteByDoctorIdAndDepartmentId(String doctorId, String departmentId);
    Integer countByDepartmentId(String departmentId);
}
