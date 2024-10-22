package com.example.bookingserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@ToString
public class Schedule extends EntityBase{
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne
            @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
            @JoinColumn(name = "specialize_id")
    Specialize specialize;
    @ManyToOne
            @JoinColumn(name = "doctor_id")
    Doctor doctor;
    LocalDateTime checkIn;
    String note;
    @Builder.Default
    boolean active=true;
}
