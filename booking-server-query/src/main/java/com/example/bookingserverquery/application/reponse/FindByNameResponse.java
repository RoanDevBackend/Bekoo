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
    int totalPage;
    long totalElements;
    List<T> contentResponse;

//    public static abstract class FindByNameResponseBuilder<C extends FindByNameResponse, B extends FindByNameResponseBuilder<C, B>> extends FindByNameQueryBuilder<C,B> {
//        public B content(List<User> users) {
//            List<UserResponse> values= new ArrayList<>();
//            for(User user: users){
//                UserResponse value= new UserResponse();
//                        value.setId(user.getId());
//                        value.setName(user.getName());
//                        value.setEmail(user.getEmail());
//                        value.setPhoneNumber(user.getPhoneNumber());
//                        value.setLinkAvatar(user.getLinkAvatar());
//                        value.setGender(user.getGender());
//                values.add(value);
//            }
//            this.content= values;
//            return self();
//        }
//    }
}
