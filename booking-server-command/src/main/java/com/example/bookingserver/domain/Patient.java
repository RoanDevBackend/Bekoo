package com.example.bookingserver.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@ToString
public class Patient extends EntityBase{
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String healthInsuranceNumber; // Số bảo hiểm y tế
    @Builder.Default
    String bloodType= "N/A"; // Nhóm máu


    @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
            @JsonIgnore
    List<MedicalHistory> medicalHistories;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
            @JsonIgnore
    List<EmergencyContact> emergencyContacts;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
            @JsonIgnore
    List<Schedule> schedules;
}
