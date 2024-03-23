package joo.project.my3dbackend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import joo.project.my3dbackend.domain.UserRefreshToken;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.dto.properties.JwtProperties;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_SPEC = "spec";
    private static final String KEY_SUBSCRIBE_STATUS = "subscribe";
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    /**
     * 토큰 발급
     */
    // TODO: spec 대신 id, userRole을 parameter로 지정
    public String generateAccessToken(String email, String nickname, String spec, SubscribeStatus subscribeStatus) {
        Map<String, String> claims = new HashMap<>();
        claims.put(KEY_EMAIL, email);
        claims.put(KEY_NICKNAME, nickname);
        claims.put(KEY_SPEC, spec); // "id:role"
        claims.put(KEY_SUBSCRIBE_STATUS, subscribeStatus.name());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.accessExpiredMs()))
                .signWith(getKey(jwtProperties.secretKey()))
                .compact();
    }

    @Transactional
    public String regenerateAccessToken(String token) throws JsonProcessingException {
        TokenInfo tokenInfo = decodeExpiredToken(token);
        // 조회하려는 refresh token은 재발행 횟수 제한보다 적게 재발행이 되어야한다.
        UserRefreshToken refreshToken = userRefreshTokenRepository
                .findByUserAccountIdAndReissueCountLessThan(tokenInfo.id(), getReissueLimit())
                .orElseThrow(() -> new AuthException(ErrorCode.EXCEED_REISSUE));
        refreshToken.increaseReissueCount();

        return generateAccessToken(
                tokenInfo.email(), tokenInfo.nickname(), tokenInfo.getSpec(), tokenInfo.subscribeStatus());
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.refreshExpiredMs()))
                .signWith(getKey(jwtProperties.secretKey()))
                .compact();
    }

    /**
     * 조회하려는 refresh token와 현재 유저의 실제 refresh token의 일치 여부 확인
     */
    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String accessToken) throws JsonProcessingException {
        parseOrValidateClaims(refreshToken); // validation
        long userAccountId = decodeExpiredToken(accessToken).id();
        // 조회하려는 refresh token은 현재 유저의 실제 refresh token과 일치해야한다.
        userRefreshTokenRepository
                .findByUserAccountIdAndReissueCountLessThan(userAccountId, getReissueLimit())
                .filter(token -> token.equalRefreshToken(refreshToken))
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_EQUAL_TOKEN));
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
                    claims.get(TokenProvider.KEY_SPEC, String.class),
                    claims.get(TokenProvider.KEY_SUBSCRIBE_STATUS, String.class));
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
        return tokenInfo.toUserPrincipal();
    }

    private TokenInfo decodeExpiredToken(String oldAccessToken) throws JsonProcessingException {
        Map<String, String> decoded = objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                new TypeReference<Map<String, String>>() {});

        return TokenInfo.of(
                decoded.get(KEY_EMAIL),
                decoded.get(KEY_NICKNAME),
                decoded.get(KEY_SPEC),
                decoded.get(KEY_SUBSCRIBE_STATUS));
    }

    private SecretKey getKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    private long getReissueLimit() {
        return jwtProperties.refreshExpiredMs() / jwtProperties.accessExpiredMs();
    }
}
