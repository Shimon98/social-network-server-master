package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.database.BlockedUserRepository;
import com.socialNetwork.server.auth.entity.LoginAttemptInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static com.socialNetwork.server.auth.utils.Constants.BLOCK_SERVICE_NAME;

@Service
public class BlockService {
    private final Cache codeCache;
    private final BlockedUserRepository blockedUserRepository;

    public BlockService(CacheManager cacheManager, BlockedUserRepository blockedUserRepository) {
        this.codeCache = cacheManager.getCache(BLOCK_SERVICE_NAME);
        this.blockedUserRepository = blockedUserRepository;
    }

    public boolean isBlocked(Long userId) {
        return blockedUserRepository.isUserBlocked(userId);
    }

    public LoginAttemptInfo getAttempts(String username) {
        Cache.ValueWrapper wrapper = codeCache.get(buildKey(username));
        if (wrapper == null) {
            return null;
        }
        return (LoginAttemptInfo) wrapper.get();
    }

    public void saveAttempts(String username, LoginAttemptInfo loginAttemptInfo) {
        codeCache.put(buildKey(username), loginAttemptInfo);
    }

    public void clearAttempts(String username) {
        codeCache.evict(buildKey(username));
    }

    public boolean blockUser(Long userId) {
        return blockedUserRepository.blockUser(userId);
    }

    public boolean unblockUser(Long userId) {
        return blockedUserRepository.unblockUser(userId);
    }

    private String buildKey(String username) {
        return username.trim().toLowerCase();
    }
}