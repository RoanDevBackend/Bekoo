package com.example.bookingserverquery.application.reponse;

import com.example.bookingserverquery.application.query.FindByNameQuery;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class FindByNameResponse<T> extends FindByNameQuery<T> {
    String name;
    int totalPage;
    long totalElements;
    List<T> contentResponse;
}
