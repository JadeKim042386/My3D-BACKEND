package joo.project.my3dbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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

    public TokenInfo parseSpecification(Claims claims) {
        try {
            return TokenInfo.of(
                    claims.get(TokenProvider.KEY_EMAIL, String.class),
                    claims.get(TokenProvider.KEY_NICKNAME, String.class),
                    claims.get(TokenProvider.KEY_SPEC, String.class));
        } catch (RuntimeException e) {
            log.error("failed token parsing");
            return null;
        }
    }

    /**
     * 토큰에 있는 정보로 UserDetails 생성
     */
    public UserPrincipal getUserDetails(Claims claims) {
        TokenInfo tokenInfo =
                Optional.ofNullable(claims).map(this::parseSpecification).orElse(TokenInfo.ofAnonymous());

        return UserPrincipal.of(tokenInfo.id(), tokenInfo.email(), tokenInfo.nickname(), tokenInfo.userRole());
    }

    private SecretKey getKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
