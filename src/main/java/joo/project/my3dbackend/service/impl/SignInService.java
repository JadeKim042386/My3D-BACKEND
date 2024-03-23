package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Subscribe;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.UserRefreshToken;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.dto.response.LoginResponse;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.UserRefreshTokenRepository;
import joo.project.my3dbackend.security.TokenProvider;
import joo.project.my3dbackend.service.SignInServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignInService implements SignInServiceInterface {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountService userAccountService;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Transactional
    public LoginResponse signIn(String email, String password) {
        UserAccount userAccount = userAccountService.getUserAccountByEmail(email);
        checkPassword(password, userAccount.getPassword());
        // 구독이 만료되었을 경우 구독 상태 변경
        if (isExpiredSubscribe(userAccount.getSubscribe())) {
            userAccount.getSubscribe().setSubscribeStatus(SubscribeStatus.STOP);
        }

        // 토큰 발급
        String accessToken = generateAccessToken(email, userAccount);
        String refreshToken = tokenProvider.generateRefreshToken();
        updateRefreshToken(userAccount.getId(), refreshToken);

        return LoginResponse.of(accessToken, refreshToken);
    }

    /**
     * 비밀번호 일치 확인
     * @param inputPassword 입력한 비밀번호
     * @param existPassword 기존 비밀번호
     */
    private void checkPassword(String inputPassword, String existPassword) {
        if (!passwordEncoder.matches(inputPassword, existPassword)) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }
    }

    /**
     * 구독 만료 여부 확인
     */
    private boolean isExpiredSubscribe(Subscribe subscribe) {
        LocalDateTime expiredAt =
                subscribe.getStartedAt().plusMonths(subscribe.getPackageType().getMonth());
        return expiredAt.isBefore(LocalDateTime.now());
    }

    /**
     * AccessToken 발급
     */
    private String generateAccessToken(String email, UserAccount userAccount) {
        return tokenProvider.generateAccessToken(
                email,
                userAccount.getNickname(),
                String.format(
                        "%s:%s", userAccount.getId(), userAccount.getUserRole().getName()),
                userAccount.getSubscribe().getSubscribeStatus());
    }

    /**
     * RefreshToken 업데이트
     */
    private void updateRefreshToken(Long id, String refreshToken) {

        userRefreshTokenRepository
                .findById(id)
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken)));
    }
}
