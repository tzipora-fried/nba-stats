package com.example.nbastats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> getPlayerStats(int playerId) {
        return (Map<String, Object>) redisTemplate.opsForHash().get("player:stats", "player:" + playerId);
    }

    public Map<String, Object> getTeamStats(int teamId) {
        return (Map<String, Object>) redisTemplate.opsForHash().get("team:stats", "team:" + teamId);
    }
}
