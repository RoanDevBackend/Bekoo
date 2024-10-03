package com.example.bookingserver.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class User extends EntityBase{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    String name;

    @Column(columnDefinition = "varchar(15)", nullable = false, unique = true)
    String phoneNumber;

    @Column(columnDefinition = "varchar(75)", nullable = false)
    String email;

    @Column(columnDefinition = "varchar(20)", nullable = false)
    String cccd;

    @Column(columnDefinition = "varchar(60)", nullable = false)
    String province;

    @Column(columnDefinition = "varchar(60)", nullable = false)
    String district;

    @Column(columnDefinition = "varchar(60)", nullable = false)
    String commune ;

    @Column(columnDefinition = "varchar(100)")
    String aboutAddress;

    @Column(columnDefinition = "varchar(75)", nullable = false)
    String password;

    @Column(columnDefinition = "date")
    LocalDate dob;
    String gender;
    String linkAvatar;

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
