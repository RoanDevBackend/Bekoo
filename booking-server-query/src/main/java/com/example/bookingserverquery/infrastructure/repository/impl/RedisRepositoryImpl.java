package com.example.bookingserverquery.infrastructure.repository.impl;

import com.example.bookingserverquery.infrastructure.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setTimeToLive(String key, Long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void hashSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public void deleteListCache(String key) {
        Set set=redisTemplate.opsForHash().entries(key).keySet();
        for(Object o:set){
            System.out.println(o.toString());
            redisTemplate.opsForHash().delete(key, o+"");
        }
    }
}
