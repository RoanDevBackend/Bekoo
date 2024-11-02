package document.event.specialize;


import document.event.department.CreateDepartmentEvent;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecializeEvent {
    String id;
    String name;
    String description;
    Integer price;
    CreateDepartmentEvent department;
}
