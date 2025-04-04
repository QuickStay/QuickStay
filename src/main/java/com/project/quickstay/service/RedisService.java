package com.project.quickstay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.quickstay.domain.place.dto.PlaceMiniInfo;
import com.project.quickstay.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveMainPageData(String key, List<PlaceMiniInfo> value) {
        String s;
        try {
            s = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ServiceException("JSON 파싱중 오류 발생");
        }
        redisTemplate.opsForValue().set(key, s);
    }

    public List<PlaceMiniInfo> getValue(String key) {
        if (!redisTemplate.hasKey(key)) { // null 체크
            return new ArrayList<>();
        }
        String s = redisTemplate.opsForValue().get(key);
        List<PlaceMiniInfo> list;
        try {
            list = Arrays.asList(objectMapper.readValue(s, PlaceMiniInfo[].class));
        } catch (JsonProcessingException e) {
            throw new ServiceException("JSON 파싱중 오류 발생");
        }
        return list;
    }
}
