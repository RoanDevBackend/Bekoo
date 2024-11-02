package document.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentResponse {
    String id;
    String name;
    String description;
    String urlImage;
    Long totalSpecialize;
    Integer totalDoctor;
}
