package study.microcoffee.gateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Configuration class of Spring sessions.
 * <p>
 * Set flush mode to immediate to commit sessions to Redis as soon as possible. Otherwise the session may not be available in Redis
 * in downstream REST services.
 */
@Configuration
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class SessionConfig {

}
