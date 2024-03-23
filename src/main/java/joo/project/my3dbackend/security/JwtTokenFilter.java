package joo.project.my3dbackend.security;

import io.jsonwebtoken.Claims;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String TOKEN_PREFIX = "Bearer";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = parseBearerToken(request, ACCESS_TOKEN_HEADER);
        try {
            if (!StringUtils.hasText(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            setAuthentication(request, tokenProvider.parseOrValidateClaims(accessToken), accessToken);
        } catch (AuthException e) {
            Optional.ofNullable(parseBearerToken(request, REFRESH_TOKEN_HEADER))
                    .ifPresentOrElse(refreshToken -> reissueAccessToken(request, response, refreshToken), () -> {
                        throw new AuthException(ErrorCode.NOT_FOUND_REFRESH_TOKEN, e);
                    });
            log.debug("reissue access token");
        } catch (Exception e) {
            log.error("Error occurs during authenticate, {}", e.getMessage());
            throw new AuthException(ErrorCode.FAILED_AUTHENTICATE);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * reqeust header의 토큰 파싱
     */
    private String parseBearerToken(HttpServletRequest request, String headerName) {
        if (headerName.equals(ACCESS_TOKEN_HEADER)) {
            return Optional.ofNullable(request.getHeader(headerName))
                    .filter(token -> !ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX))
                    .map(token -> token.substring(TOKEN_PREFIX.length()).trim())
                    .orElse(null);
        } else {
            return request.getHeader(headerName);
        }
    }

    /**
     * Set Authentication
     */
    private void setAuthentication(HttpServletRequest request, Claims claims, String accessToken) {
        UserPrincipal memberPrincipal = tokenProvider.getUserDetails(claims);
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
                memberPrincipal, accessToken, memberPrincipal.getAuthorities());
        authenticated.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }

    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        try {
            String oldAccessToken = parseBearerToken(request, ACCESS_TOKEN_HEADER);
            tokenProvider.validateRefreshToken(refreshToken, oldAccessToken);
            String newAccessToken = tokenProvider.regenerateAccessToken(oldAccessToken);
            setAuthentication(request, tokenProvider.parseOrValidateClaims(newAccessToken), newAccessToken);
            response.setHeader("New-Access-Token", newAccessToken);
        } catch (Exception e) {
            throw new AuthException(ErrorCode.FAILED_REISSUE_ACCESS_TOKEN);
        }
    }
}
