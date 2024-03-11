package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.UserAccountRepository;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAccountService implements UserAccountServiceInterface {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserAccount getUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email).orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    @Override
    public void registerUser(UserAccount userAccount) {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
    }

    private String encodePassword(String password) {
        return encoder.encode(password);
    }
}
