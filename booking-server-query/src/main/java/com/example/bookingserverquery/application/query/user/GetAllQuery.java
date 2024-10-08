package com.example.bookingserverquery.application.query.user;

import com.example.bookingserverquery.application.query.QueryBase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class GetAllQuery extends QueryBase {

}
