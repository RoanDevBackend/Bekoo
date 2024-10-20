package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Document(indexName = "user") @ToString
public class User extends EntityBase {
    @Id
    String id;

    @Field(type = FieldType.Text)
    String name;

    @Field(type = FieldType.Keyword)
    String phoneNumber;

    @Field(type = FieldType.Keyword)
    String email;

    @Field(type = FieldType.Keyword)
    String cccd;

    @Field(type = FieldType.Text)
    String province;

    @Field(type = FieldType.Text)
    String district;

    @Field(type = FieldType.Text)
    String commune ;

    @Field(type = FieldType.Text)
    String aboutAddress;

    @Field(type = FieldType.Keyword)
    String password;

    @Field(type = FieldType.Date)
    LocalDate dob;

    @Field(type = FieldType.Keyword)
    String gender;

    @Field(type = FieldType.Keyword)
    String linkAvatar;

    @Field(type = FieldType.Keyword)
    Set<String> roles;

    public static abstract class UserBuilder<C extends User, B extends UserBuilder<C, B>> extends EntityBaseBuilder<C,B>{
        public B email(String email) {
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new IllegalArgumentException("Email không hợp lệ");
            }
            this.email= email;
            return self();
        }
        public B phoneNumber(String phoneNumber){
            if(!phoneNumber.matches("^0[3|5|7|8|9][0-9]{8}$")){
                throw new IllegalArgumentException("Định dạng số điện thoại không đúng");
            }
            this.phoneNumber= phoneNumber;
            return self();
        }
    }
}
