package com.example.demo2.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimiterConfig {

    // Giới hạn: 10 request trong 1 phút
    private final int CAPACITY = 10;
    private final Refill REFILL = Refill.intervally(10, Duration.ofMinutes(1));

    // Lưu bucket cho từng IP (hoặc user)
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Bean
    public Map<String, Bucket> getBuckets() {
        return buckets;
    }

    // Tạo bucket mới nếu chưa có
    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::newBucket);
    }

    // Tạo bucket với giới hạn
    private Bucket newBucket(String apiKey) {
        Bandwidth limit = Bandwidth.classic(CAPACITY, REFILL);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}