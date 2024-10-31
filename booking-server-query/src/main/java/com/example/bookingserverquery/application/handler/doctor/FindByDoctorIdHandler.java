package com.example.bookingserverquery.application.handler.doctor;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.application.service.i.DoctorService;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.repository.DoctorRepository;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindByDoctorIdHandler {

    final DoctorMapper doctorMapper;
    final DoctorRepository doctorRepository;
    final DoctorService doctorService;

    @SneakyThrows
    public DoctorResponse execute(String id){
        Optional<Doctor> doctorOptional= doctorRepository.findById(id);
        Doctor doctor= doctorOptional.orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        return doctorService.toResponse(doctor);
    }
}
