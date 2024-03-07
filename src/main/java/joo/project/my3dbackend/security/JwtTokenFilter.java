package joo.project.my3dbackend.security;

import io.jsonwebtoken.Claims;
import joo.project.my3dbackend.dto.security.UserPrincipal;
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
    public static final String TOKEN_PREFIX = "Bearer";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = parseBearerToken(request);
        try {
            if (!StringUtils.hasText(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            setAuthentication(request, tokenProvider.parseOrValidateClaims(accessToken), accessToken);
        } catch (Exception e) {
            log.error("Error occurs during authenticate, {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    /**
     * reqeust header의 토큰 파싱
     */
    private String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_HEADER))
                .filter(token -> !ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX))
                .map(token -> token.substring(TOKEN_PREFIX.length()).trim())
                .orElse(null);
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
}
