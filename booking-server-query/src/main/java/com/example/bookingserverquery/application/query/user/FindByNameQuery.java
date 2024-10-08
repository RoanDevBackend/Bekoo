package com.example.bookingserverquery.application.query.user;

import com.example.bookingserverquery.application.query.QueryBase;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FindByNameQuery extends QueryBase {
    String name;
}
