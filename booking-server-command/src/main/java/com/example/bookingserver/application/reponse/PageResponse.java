package com.example.bookingserver.application.reponse;

import com.example.bookingserver.application.query.QueryBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class PageResponse<T> extends QueryBase<T> {

    private int totalPage;
    private List<T> contentResponse;

}
