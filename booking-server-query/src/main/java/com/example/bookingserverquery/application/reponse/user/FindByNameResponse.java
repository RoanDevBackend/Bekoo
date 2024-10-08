package com.example.bookingserverquery.application.reponse.user;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.query.user.FindByNameQuery;
import com.example.bookingserverquery.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class FindByNameResponse extends FindByNameQuery {
    int totalPage;
    List<Value> content;

    public static abstract class FindByNameResponseBuilder<C extends FindByNameResponse, B extends FindByNameResponseBuilder<C, B>> extends FindByNameQueryBuilder<C,B> {
        public B content(List<User> users) {
            List<Value> values= new ArrayList<>();
            for(User user: users){
                Value value= Value.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .linkAvatar(user.getLinkAvatar())
                        .gender(user.getGender())
                        .build();
                values.add(value);
            }
            this.content= values;
            return self();
        }
    }



}
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
class Value{
    String id;
    String name;
    String email;
    String phoneNumber;
    String linkAvatar;
    String gender;
}