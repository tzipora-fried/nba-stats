package com.example.nbastats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Map<Object, Object> getPlayerStats(int playerId, String seasonYear) {
        String redisKey = "player:" + playerId + ":season:" + seasonYear;
        return redisTemplate.opsForHash().entries(redisKey);
    }

    public Map<Object, Object> getTeamStats(int teamId, String seasonYear) {
        String redisKey = "team:" + teamId + ":season:" + seasonYear;
        return redisTemplate.opsForHash().entries(redisKey);
    }

}
