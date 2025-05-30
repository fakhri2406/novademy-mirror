package com.novademy.application.external.token;

import com.novademy.application.config.JwtProperties;
import com.novademy.application.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenGeneratorImpl implements TokenGenerator {
    private final Key key;
    private final JwtProperties props;

    public TokenGeneratorImpl(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + props.getAccessTokenValiditySeconds() * 1000);
        return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("id", user.getId().toString())
            .claim("role", user.getRole().getName())
            .claim("firstName", user.getFirstName())
            .claim("lastName", user.getLastName())
            .claim("email", user.getEmail())
            .claim("phoneNumber", user.getPhoneNumber())
            .claim("group", user.getGroup())
            .claim("sector", user.getSector().name())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
} 