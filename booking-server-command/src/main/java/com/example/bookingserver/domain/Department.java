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
public class Department extends EntityBase{
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;
    String urlImage;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
            @JsonIgnore
    List<DoctorDepartment> doctorDepartments;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
            @JsonIgnore
    List<Specialize> specialities;
}
