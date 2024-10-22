package com.example.bookingserverquery.application.reponse;

import com.example.bookingserverquery.application.query.QueryBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class PageResponse<T> extends QueryBase<T> {

    int totalPage;
    long totalElements;
    List<T> contentResponse;

}
