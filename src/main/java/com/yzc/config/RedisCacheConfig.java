package com.yzc.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis缓存配置文件
 * @author yzc
 * @date 2016年11月3日
 *@version 0.1
 */
@Configuration
@EnableCaching//启用注解驱动的缓存
public class RedisCacheConfig extends CachingConfigurationSelector{

	@Bean
    public JedisConnectionFactory redisConnectionFactory() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(Integer.valueOf(WebApplicationInitializer.redis_properties.getProperty("redis.pool.maxIdle")));
        jedisPoolConfig.setMinIdle(Integer.valueOf(WebApplicationInitializer.redis_properties.getProperty("redis.pool.minIdle")));
        jedisPoolConfig.setMaxWaitMillis(Long.valueOf(WebApplicationInitializer.redis_properties.getProperty("redis.pool.maxWait")));
        jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(WebApplicationInitializer.redis_properties.getProperty("redis.pool.testOnBorrow")));

        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();  
        redisConnectionFactory.setHostName(WebApplicationInitializer.redis_properties.getProperty("redis.host"));  
        redisConnectionFactory.setPort(Integer.valueOf(WebApplicationInitializer.redis_properties.getProperty("redis.port")));
        redisConnectionFactory.setDatabase(Integer.valueOf(WebApplicationInitializer.redis_properties.getProperty("redis.dbIndex")));
        redisConnectionFactory.setUsePool(true);
        redisConnectionFactory.setPoolConfig(jedisPoolConfig);

        return redisConnectionFactory;  
    }  
  
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory cf) {  
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();  
        redisTemplate.setConnectionFactory(cf);  
    	StringRedisTemplate srt = new StringRedisTemplate();
    	srt.setConnectionFactory(cf);
        redisTemplate.afterPropertiesSet();
        return srt;
    }   

    @Bean
    public CacheManager cacheManager(StringRedisTemplate redisTemplate) {  
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);  
        cacheManager.setDefaultExpiration(3600);
	    return cacheManager;  
	}  
}
