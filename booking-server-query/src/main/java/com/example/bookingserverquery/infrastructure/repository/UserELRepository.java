package com.example.bookingserverquery.infrastructure.repository;

import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import com.example.bookingserverquery.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserELRepository extends ElasticsearchRepository<User, String> {

    @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    Page<User> findUsersByName(String name, Pageable pageable);
}
