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
@SuperBuilder @ToString
public class Doctor extends EntityBase{
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "text")
    String trainingBy;

    @Column(columnDefinition = "text")
    String description;

    Integer price;

    @Builder.Default
    Integer maximumPeoplePerDay=0;

    @OneToOne()
            @JoinColumn(name = "user_id", unique = true)
    User user;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
            @JsonIgnore
    List<DoctorDepartment> doctorDepartments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
            @JsonIgnore
    List<Schedule> schedules;
}
