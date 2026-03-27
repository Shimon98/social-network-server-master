package com.socialNetwork.server.auth.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.socialNetwork.server.auth.utils.Constants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager manager =
                new CaffeineCacheManager("users",
                        "verificationCodes",
                        "userPosts", Constants.BLOCK_SERVICE_NAME
                );

        manager.setCaffeine(
                Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(1000)
        );
        return manager;
    }
}

