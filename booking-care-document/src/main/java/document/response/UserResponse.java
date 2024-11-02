package document.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String name;
    String phoneNumber;
    String email;
    String cccd;
    String province;
    String district;
    String commune ;
    String aboutAddress;
    LocalDate dob;
    String gender;
    String linkAvatar;
    Set<String> roles;
}
