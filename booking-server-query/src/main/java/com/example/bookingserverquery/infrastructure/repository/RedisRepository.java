package com.example.bookingserverquery.infrastructure.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository {
    void setTimeToLive(String key , Long time) ;
    void set(String key, Object value);
    Object get(String key);
    void delete(String key) ;
    void hashSet(final String key, final String field, Object value);
    Object hashGet(final String key, final String field);
    void deleteListCache(String key);
}
