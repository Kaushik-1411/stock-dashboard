package stock_dashboard.stock_dashboard.config;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

public class RedisConfig {

    public RedisCacheManager cacheManager(RedisConnectionFactory factory){
        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig();

        return RedisCacheManager.builder(factory)
                .withInitialCacheConfigurations(Map.of(
                        "stockPrice",     defaults.entryTtl(Duration.ofMinutes(2)),
                        "stockHistory", defaults.entryTtl(Duration.ofMinutes(10)),
                        "recommendation", defaults.entryTtl(Duration.ofMinutes(5))
                ))
                .build();
    }
}
