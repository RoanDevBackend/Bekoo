package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
    public void set(String key, int value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Integer get(String key) {
        return (Integer) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
