package document.event.doctor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoDoctorEvent {
    String id;
    String trainingBy;
    String description;
    LocalDateTime updatedAt;
}
