package com.team5.projrental.common.redis;

import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.user.verification.users.model.VerificationRedisForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.config.host}")
    private String host;

    @Value("${redis.config.port}")
    private Integer port;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);

    }

    @Bean
    public RedisTemplate<String, VerificationRedisForm> redisTemplate() {
        RedisTemplate<String, VerificationRedisForm> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(VerificationRedisForm.class));
        return redisTemplate;
    }

}
