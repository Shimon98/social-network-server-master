package com.socialNetwork.server.auth.services;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


@Service
public class BlockService {
    private Cache codeCache;


    public BlockService(CacheManager cacheManager) {
        this.codeCache = cacheManager.getCache("");

    }
}
