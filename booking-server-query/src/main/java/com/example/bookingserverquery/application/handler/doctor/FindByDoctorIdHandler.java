package com.example.bookingserverquery.application.handler.doctor;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.application.reponse.user.FindByIdResponse;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.domain.repository.DoctorRepository;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindByDoctorIdHandler {

    final DoctorMapper doctorMapper;
    final DoctorRepository doctorRepository;
    @SneakyThrows
    public DoctorResponse execute(String id){
        Optional<Doctor> doctorOptional= doctorRepository.findById(id);
        Doctor doctor= doctorOptional.orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        return doctorMapper.toResponse(doctor, doctor.getUser());
    }
}
