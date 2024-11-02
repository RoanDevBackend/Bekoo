package document.event.doctor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetMaximumPeoplePerDayEvent {
    String id;
    Integer value;
    LocalDateTime updatedAt;
}
