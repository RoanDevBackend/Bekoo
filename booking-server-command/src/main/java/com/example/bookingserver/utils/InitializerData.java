package com.example.bookingserver.utils;

import com.example.bookingserver.application.command.command.schedule.InitScheduleCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.domain.Schedule;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitializerData implements CommandLineRunner {
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final SpecializeRepository specializeRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Override
    public void run(String... args) {
        log.info("Bắt đầu khởi tạo dữ liệu Schedule từ file CSV...");

        List<InitScheduleCommand> commands = readCsv("data/schedule_data.csv");

        if (commands.isEmpty()) return;

        int countInsert = 0;
        int countSkip = 0;

        for (InitScheduleCommand cmd : commands) {
            Patient patient = patientRepository.findById(cmd.getPatientId())
                    .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_PATIENT_NOT_EXISTED));
            Doctor doctor = doctorRepository.findById(cmd.getDoctorId())
                    .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_DOCTOR_NOT_EXISTED));
            Specialize specialize = specializeRepository.findById(cmd.getSpecializeId())
                    .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED));

            LocalDateTime checkIn = parseDateTime(cmd.getCheckIn());
            LocalDateTime createdAt = parseDateTime(cmd.getCreateAt());

            if (scheduleRepository.existsByCheckInAndCreatedAt(checkIn, createdAt)) {
                log.info("Bản ghi đã tồn tại (CheckIn: {}, CreatedAt: {}). Bỏ qua.", checkIn, createdAt);
                countSkip++;
                continue;
            }
//            if (checkIn.isBefore(LocalDateTime.now())) {
//                throw new BookingCareException(ErrorDetail.ERR_SCHEDULE_DATE);
//            }

//            int personPerDay = doctor.getMaximumPeoplePerDay();
//            System.out.println(personPerDay);
//
//            if (personPerDay + 1 > doctor.getMaximumPeoplePerDay()) {
//                throw new BookingCareException(ErrorDetail.ERR_DOCTOR_PERSON);
//            }


            Schedule newSchedule = Schedule.builder()
                    .id(cmd.getId())
                    .createdAt(parseDateTime(cmd.getCreateAt()))
                    .updatedAt(parseDateTime(cmd.getUpdatedAt()))
                    .checkIn(checkIn)
                    .note(cmd.getNote())
                    .paymentStatus(cmd.getPaymentStatus())
                    .statusId(cmd.getStatusId())
                    .doctor(doctor)
                    .patient(patient)
                    .specialize(specialize)
                    .build();

            scheduleRepository.save(newSchedule);

            countInsert++;
        }

        log.info("Kết thúc khởi tạo: Thêm mới {} | Bỏ qua {} | Tổng xử lý {}", countInsert, countSkip, commands.size());
    }

    private List<InitScheduleCommand> readCsv(String fileName) {
        List<InitScheduleCommand> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource(fileName).getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(";");

                if (fields.length < 10) continue;

                InitScheduleCommand command = new InitScheduleCommand();
                command.setId(cleanField(fields[0]));
                command.setCreateAt(cleanField(fields[1]));
                command.setUpdatedAt(cleanField(fields[2]));
                command.setCheckIn(cleanField(fields[3]));

                command.setNote(fields.length > 4 ? cleanField(fields[4]) : null);

                command.setPaymentStatus(parseIntSafe(fields[5]));
                command.setStatusId(parseIntSafe(fields[6]));

                command.setDoctorId(cleanField(fields[7]));
                command.setPatientId(cleanField(fields[8]));
                command.setSpecializeId(cleanField(fields[9]));

                dataList.add(command);
            }
        } catch (IOException e) {
            log.error("Lỗi khi đọc file CSV: ", e);
        }

        return dataList;
    }

    private String cleanField(String field) {
        return (field == null || field.trim().isEmpty()) ? null : field.trim();
    }

    private int parseIntSafe(String field) {
        try {
            return Integer.parseInt(field.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return LocalDateTime.parse(dateStr, FORMATTER);
        } catch (Exception e) {
            log.error("Lỗi parse ngày tháng: {}", dateStr);
            return LocalDateTime.now();
        }
    }
}
