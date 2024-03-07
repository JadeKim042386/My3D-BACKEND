package joo.project.my3dbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.security.Keys;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.properties.JwtProperties;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_SPEC = "spec";
    private static final String ANONYMOUS = "ANONYMOUS";
    private final JwtProperties jwtProperties;

    /**
     * 토큰 발급
     */
    public String generateAccessToken(String email, String nickname, String spec) {
        Map<String, String> claims = new HashMap<>();
        claims.put(KEY_EMAIL, email);
        claims.put(KEY_NICKNAME, nickname);
        claims.put(KEY_SPEC, spec); // "id:role"

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.accessExpiredMs()))
                .signWith(getKey(jwtProperties.secretKey()))
                .compact();
    }

    /**
     * 토큰 파싱
     */
    public Claims parseOrValidateClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey(jwtProperties.secretKey()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.EXPIRED_TOKEN, e);
        }
    }

    /**
     * @return {id, email, nickname, authority}
     */
    public String[] parseSpecification(Claims claims) {
        try {
            String[] spec = claims.get(TokenProvider.KEY_SPEC, String.class).split(":");
            return new String[] {
                spec[0], // id
                claims.get(TokenProvider.KEY_EMAIL, String.class),
                claims.get(TokenProvider.KEY_NICKNAME, String.class),
                spec[1] // authority
            };
        } catch (RequiredTypeException e) {
            return null;
        }
    }

    /**
     * 토큰에 있는 정보로 UserDetails 생성
     */
    public UserPrincipal getUserDetails(Claims claims) {
        String[] parsed = Optional.ofNullable(claims)
                .map(this::parseSpecification)
                .orElse(new String[] {null, ANONYMOUS, ANONYMOUS, ANONYMOUS});

        return UserPrincipal.of(
                Long.parseLong(parsed[0]), parsed[1], parsed[2], UserRole.valueOf(parsed[3])); // id, email, nickname, authority
    }

    private SecretKey getKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
