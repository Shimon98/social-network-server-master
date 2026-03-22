package com.socialNetwork.server.login.services;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


@Service
public class BlockServise {
    private Cache codeCache;


    public BlockServise(CacheManager cacheManager) {
        this.codeCache = cacheManager.getCache("");

    }
}
