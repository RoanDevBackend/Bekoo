package document.event.schedule;


import lombok.*;
import lombok.experimental.FieldDefaults;
import document.event.EventBase;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleEvent extends EventBase {
    String id;
    String patientId;
    String doctorId;
    String specializeId;
    LocalDateTime checkIn;
    String note;
    String status;
}
