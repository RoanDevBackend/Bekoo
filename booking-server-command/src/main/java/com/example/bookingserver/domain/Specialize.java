package com.example.bookingserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@ToString
public class Specialize extends EntityBase{
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(columnDefinition = "varchar(150)")
    String name;
    @Column(columnDefinition = "text")
    String description;
    @ManyToOne
            @JoinColumn(name = "department_id")
    Department department;
}
