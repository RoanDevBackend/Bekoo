package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.persistence.repository.SpecializeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SpecializeRepositoryImpl implements SpecializeRepository {

    SpecializeJpaRepository specializeJpaRepository;

    @Override
    public Specialize save(Specialize specialize) {
        return specializeJpaRepository.save(specialize);
    }

    @Override
    public void delete(String id) {
        specializeJpaRepository.deleteById(id);
    }

    @Override
    public void delete(Specialize specialize) {
        specializeJpaRepository.delete(specialize);
    }

    @Override
    public Optional<Specialize> findById(String id) {
        return specializeJpaRepository.findById(id);
    }

    @Override
    public Page<Specialize> findByDepartment(String departmentId, Pageable pageable) {
        return specializeJpaRepository.findByDepartmentId(departmentId, pageable);
    }

    @Override
    public Page<Specialize> findAll(Pageable pageable) {
        return specializeJpaRepository.findAll(pageable);
    }
}
