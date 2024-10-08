package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import com.example.bookingserverquery.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserELRepository extends ElasticsearchRepository<User, String> {

    Page<User> findUsersByName(String name, Pageable pageable);

}
