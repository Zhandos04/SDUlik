package backend.project.sdulik.services;

import java.util.Date;

public interface TokenBlacklistService {
    void addTokenToBlacklist(String token, Date expirationTime);
    boolean isTokenBlacklisted(String token);

}
