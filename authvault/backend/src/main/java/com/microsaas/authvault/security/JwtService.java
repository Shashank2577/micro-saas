package com.microsaas.authvault.security;

import com.microsaas.authvault.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyWithAtLeast32BytesLengthForHS256Algo1234567890}")
    private String secret;

    @Value("${jwt.expiration:900000}") // 15 mins default
    private long jwtExpiration;

    @Value("${jwt.refreshExpiration:86400000}") // 1 day
    private long refreshExpiration;

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user, jwtExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(new HashMap<>(), user, refreshExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, User user, long expiration) {
        extraClaims.put("tenant_id", user.getTenantId().toString());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
