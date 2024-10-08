package com.example.bookingserverquery.application.event.models.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAvatarUserEvent {
    String id;
    String linkImage;
}
