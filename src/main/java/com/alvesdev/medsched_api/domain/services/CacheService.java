package com.alvesdev.medsched_api.domain.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    
    @Autowired
    private CacheManager cacheManager;

    public void evictAllCaches(String cacheName) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
    }
}
