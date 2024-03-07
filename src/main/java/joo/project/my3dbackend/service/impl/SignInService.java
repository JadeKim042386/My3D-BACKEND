package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountService userAccountService;

    public String signIn(String email, String password) {
        UserAccount userAccount = userAccountService.getUserAccountByEmail(email);
        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(password, userAccount.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }
        // AccessToken 발급
        return tokenProvider.generateAccessToken(
                email,
                userAccount.getNickname(),
                String.format(
                        "%s:%s", userAccount.getId(), userAccount.getUserRole().getName()));
    }
}
