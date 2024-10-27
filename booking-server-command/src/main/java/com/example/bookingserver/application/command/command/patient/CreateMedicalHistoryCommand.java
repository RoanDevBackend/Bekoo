package com.example.bookingserver.application.command.command.patient;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMedicalHistoryCommand {
    String patientId;
    String name;
    String dateOfVisit; // Ngày khám
    String diagnosis; // Chẩn đoán
    String treatment; // Điều trị hoặc ghi chú
    String doctorNotes; // Ghi chú của bác sĩ
    String prescribedMedication; // Thuốc được kê đơn
}
