package document.event.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoUserEvent {
    String id;
    String name;
    String province;
    String district;
    String commune;
    String aboutAddress;
}
