package com.budgeting.backend.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.bson.types.ObjectId;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CatchingConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache inviteCodes = new CaffeineCache(
                "inviteCodes",
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(10_000)
                        .build()
        );

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(inviteCodes));
        return manager;
    }
    public record InviteInfo(ObjectId houseHoldId, String code) {}
}


