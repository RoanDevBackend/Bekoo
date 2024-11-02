package document.event.doctor;

import document.event.user.CreateUserEvent;
import lombok.*;
import lombok.experimental.FieldDefaults;
import document.event.EventBase;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDoctorEvent extends EventBase {
    String id;
    String trainingBy;
    String description;
    CreateUserEvent user;
}
