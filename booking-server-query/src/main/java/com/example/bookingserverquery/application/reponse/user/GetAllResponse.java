package com.example.bookingserverquery.application.reponse.user;

import com.example.bookingserverquery.application.query.QueryBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class GetAllResponse extends QueryBase {

    int totalPage;
    List<UserResponse> userResponses;

}
