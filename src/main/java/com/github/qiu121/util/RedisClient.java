package com.github.qiu121.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author to_Geek
 * @version 1.0
 * @date 2023/10/02
 * @desc Redis操作工具类
 */
@Component
@Slf4j
public class RedisClient<T> {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加Redis数据
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @throws JsonProcessingException 序列化异常
     */
    public void set(String key, T value, Long timeout, TimeUnit timeUnit) throws JsonProcessingException {
        // 使用Jackson序列化为JSON对象
        ObjectMapper mapper = new ObjectMapper();
        /*
        Java 8 date/time type `java.time.LocalDate` not supported by default:
        add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
        */
        mapper.registerModule(new JavaTimeModule());
        String string = mapper.writeValueAsString(value);
        stringRedisTemplate.opsForValue().set(key, string, timeout, timeUnit);
    }

    /**
     * 获取分页查询的缓存数据
     *
     * @param key           键
     * @param typeReference 泛型对象引用，Jackson无法序列化泛型对象
     * @return T 泛型类型
     * @throws JsonProcessingException JSON处理异常
     */
    @SuppressWarnings("unchecked")
    public T get(String key, TypeReference<T> typeReference) throws JsonProcessingException {
        String json = stringRedisTemplate.opsForValue().get(key);
        // 如果键不存在，直接返回原始值null,否则会出现序列化异常(为null值反序列化)
        if (json == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // 利用 TypeReference 获取泛型的 JavaType
        JavaType javaType = mapper.getTypeFactory().constructType(typeReference);
        // 会出现泛型擦除警告
        Class<T> rawClass = (Class<T>) javaType.containedType(0).getRawClass();

        // 仍然保持 Page.class 用于指定 IPage 具体实现类型
        JavaType iPageType = mapper.getTypeFactory().constructParametricType(Page.class, rawClass);
        return mapper.readValue(json, iPageType);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }


}
