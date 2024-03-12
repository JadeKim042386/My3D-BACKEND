package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.UserAccountRepository;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService implements UserAccountServiceInterface {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    @Override
    public UserAccount getUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email).orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public void registerUser(UserAccount userAccount) {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
    }

    @Override
    public void updateUser(String email, AdminRequest adminRequest) {
        UserAccount userAccount = getUserAccountByEmail(email);
        Optional.ofNullable(adminRequest.nickname()).ifPresent(userAccount::setNickname);
        Optional.ofNullable(adminRequest.phone()).ifPresent(userAccount::setPhone);
        Optional.of(adminRequest.getAddress()).ifPresent(userAccount::setAddress);
    }

    @Override
    public void updatePassword(String email, PasswordRequest passwordRequest) {
        UserAccount userAccount = getUserAccountByEmail(email);
        userAccount.setPassword(passwordRequest.password());
    }

    private String encodePassword(String password) {
        return encoder.encode(password);
    }
}
