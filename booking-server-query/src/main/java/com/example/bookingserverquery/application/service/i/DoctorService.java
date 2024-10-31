package com.example.bookingserverquery.application.service.i;

import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.domain.Doctor;
import org.springframework.stereotype.Service;

@Service
public interface DoctorService {
    Long getTotalPatientsVisited(String doctorId);
    DoctorResponse toResponse(Doctor doctor);
}
