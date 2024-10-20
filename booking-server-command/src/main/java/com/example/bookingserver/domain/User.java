package com.example.bookingserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class User extends EntityBase implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    String name;

    @Column(columnDefinition = "varchar(15)", nullable = false)
    String phoneNumber;

    @Column(columnDefinition = "varchar(75)", nullable = false, unique = true)
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
            @JsonIgnore
    Doctor doctor;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles=new HashSet<>();


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> role=  new ArrayList<>();
        for(Role x : roles){
            role.add(new SimpleGrantedAuthority(x.getName()));
        }
        return role;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
