package document.event.patient;

import document.event.EventBase;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PatientEvent extends EventBase {
    String id;
    String healthInsuranceNumber;
    String bloodType;
    String userId;
    EmergencyContactEvent emergencyContact;
}
