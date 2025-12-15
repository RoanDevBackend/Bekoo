package com.example.bookingserver.application.command.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    // Lưu trữ bucket cho từng API key hoặc IP (trong thực tế nên dùng Cache như Redis)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String name) {
        return cache.computeIfAbsent(name, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        // Cấu hình: Cho phép 10 request, refill lại 10 token sau mỗi 1 phút
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}