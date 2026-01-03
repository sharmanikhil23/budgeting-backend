package com.budgeting.backend.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CachingConfig {

    public static final String INVITE_CODES_CACHE = "inviteCodes";

    @Bean
    public CacheManager cacheManager( @Value("${cache.life}") long life) {
        CaffeineCache inviteCodes = new CaffeineCache(
                INVITE_CODES_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(life, TimeUnit.MILLISECONDS)
                        .maximumSize(10_000)
                        .recordStats() // useful later for monitoring
                        .build()
        );

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(inviteCodes));
        return manager;
    }

    /**
     * One-time-use invite info
     */
    public record InviteInfo(ObjectId houseHoldId) {}
}
