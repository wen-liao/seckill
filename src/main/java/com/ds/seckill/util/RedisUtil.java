package com.ds.seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    public static String ProductKey = "product";

    private static String combine(String key, String subKey){
        String compoundKey = new StringBuilder(key).append(":").append(subKey).toString();
        logger.info("key: {}", compoundKey);
        return compoundKey;
    }

    public static void put(RedisTemplate<String, Object> redisTemplate, String key, String subKey, Object value){
        String compoundKey = combine(key, subKey);
        redisTemplate.opsForValue().set(compoundKey, value);
    }

    public static Object get(RedisTemplate<String, Object> redisTemplate, String key, String subKey){
        String compoundKey = combine(key, subKey);
        return redisTemplate.opsForValue().get(compoundKey);
    }

    public static void increment(RedisTemplate<String, Object> redisTemplate, String key, String subKey, Integer value){
        String compoundKey = combine(key, subKey);
        redisTemplate.opsForValue().increment(compoundKey, value);
    }

    public static void watch(RedisTemplate<String, Object> redisTemplate, String key, String subKey){
        String compoundKey = combine(key, subKey);
        redisTemplate.watch(compoundKey);
    }

    //TODO:
    // design of redis schema
    // key: 'product'
    // value : Hash Map (key: productId, value: count)
    // operation:
    // 1. void put(Integer productId, Integer count)
    // 2. boolean get(Integer productId)

}
