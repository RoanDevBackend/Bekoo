package com.example.bookingserverquery.application.reponse;

import com.example.bookingserverquery.application.query.QueryBase;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> extends QueryBase<T> {
    int totalPage;
    long totalElements;
    List<T> contentResponse;

}
