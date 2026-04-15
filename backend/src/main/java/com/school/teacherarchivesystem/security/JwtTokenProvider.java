package com.school.teacherarchivesystem.security;

import com.school.teacherarchivesystem.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final AppProperties appProperties;

    public JwtTokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    private SecretKey key() {
        String secret = appProperties.getJwt().getSecret();
        while (secret.length() < 32) {
            secret = secret + "_" + secret;
        }
        return Keys.hmacShaKeyFor(secret.substring(0, 32).getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(LoginUser loginUser) {
        Instant now = Instant.now();
        Instant expireAt = now.plusSeconds(appProperties.getJwt().getExpirationSeconds());
        return Jwts.builder()
                .subject(loginUser.getUsername())
                .claim("uid", loginUser.getUserId())
                .claim("name", loginUser.getRealName())
                .claim("role", loginUser.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(key())
                .compact();
    }

    public LoginUser parseToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(claims.getSubject());
        loginUser.setUserId(claims.get("uid", Long.class));
        loginUser.setRealName(claims.get("name", String.class));
        loginUser.setRole(com.school.teacherarchivesystem.enums.UserRole.valueOf(claims.get("role", String.class)));
        return loginUser;
    }
}
