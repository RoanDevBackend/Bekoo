package document.event.patient;

import lombok.*;
import lombok.experimental.FieldDefaults;
import document.event.EventBase;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalHistoryEvent extends EventBase {
    Long id;
    String name;
    String patientId;
    LocalDate dateOfVisit;
    String diagnosis;
    String treatment;
    String doctorNotes;
    String prescribedMedication;
}
