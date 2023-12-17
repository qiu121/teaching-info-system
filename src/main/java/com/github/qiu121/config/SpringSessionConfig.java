package com.github.qiu121.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * SpringSession自定义Redis序列化配置
 *
 * @author to_Geek
 * @version 1.0
 * @date 2023/12/17
 */
@Configuration
public class SpringSessionConfig {
    /*
       RedisHttpSessionConfiguration中，
       Bean名称必须是springSessionDefaultRedisSerializer才能指定Jackson序列化器。
   */
    @Bean
    @Qualifier("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> setDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
