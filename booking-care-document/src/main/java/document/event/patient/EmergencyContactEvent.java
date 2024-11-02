package document.event.patient;

import lombok.*;
import lombok.experimental.FieldDefaults;
import document.event.EventBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmergencyContactEvent extends EventBase {
    Long id;
    String name;
    String phone;
    String address;
    String relationship;
    String patientId;
}
