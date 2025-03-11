package com.example.nbastats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PlayerService  playerService;

    public Map<Object, Object> getPlayerStats(String playerId, String seasonYear) {
        Integer id = playerService.getPlayerIdByIdNumber(playerId);
        String redisKey = "player:" + id + ":season:" + seasonYear;
        return redisTemplate.opsForHash().entries(redisKey);
    }

    public Map<Object, Object> getTeamStats(String teamName, String seasonYear) {
        Integer teamId = playerService.getTeamIdByName(teamName);
        String redisKey = "team:" + teamId + ":season:" + seasonYear;
        return redisTemplate.opsForHash().entries(redisKey);
    }

}
