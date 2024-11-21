package backend.project.sdulik.services.impl;

import backend.project.sdulik.services.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenBlacklistServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Добавить токен в черный список.
     * @param token Токен, который нужно занести в черный список.
     * @param expirationTime Время истечения токена.
     */
    public void addTokenToBlacklist(String token, Date expirationTime) {
        long timeToLive = expirationTime.getTime() - System.currentTimeMillis();
        if (timeToLive > 0) {
            redisTemplate.opsForValue().set(token, "blacklisted", timeToLive, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Проверить, находится ли токен в черном списке.
     * @param token Токен, который нужно проверить.
     * @return True, если токен находится в черном списке.
     */
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
