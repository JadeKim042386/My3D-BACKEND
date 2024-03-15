package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Address;
import joo.project.my3dbackend.domain.Company;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.SignUpException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.UserAccountRepository;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

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
        checkIfValidCompany(userAccount.getCompany());
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
    }

    @Override
    public void updateUser(String email, AdminRequest adminRequest) {
        UserAccount userAccount = getUserAccountByEmail(email);
        if (isUpdatedUserData(userAccount.getNickname(), adminRequest.nickname())) {
            userAccount.setNickname(adminRequest.nickname());
        }
        if (isUpdatedUserData(userAccount.getPhone(), adminRequest.phone())) {
            userAccount.setPhone(adminRequest.phone());
        }
        Address updatedAddress = adminRequest.getAddress();
        if (isUpdatedUserData(userAccount.getAddress(), updatedAddress)) {
            userAccount.setAddress(updatedAddress);
        }
    }

    @Override
    public void updatePassword(String email, PasswordRequest passwordRequest) {
        UserAccount userAccount = getUserAccountByEmail(email);
        userAccount.setPassword(passwordRequest.password());
    }

    private String encodePassword(String password) {
        return encoder.encode(password);
    }

    private <T> boolean isUpdatedUserData(T savedData, T updatedData) {
        return !savedData.equals(updatedData);
    }

    /**
     * 기업에 대한 Validation 수행
     */
    private void checkIfValidCompany(Company company) {
        if (!Objects.isNull(company) && !StringUtils.hasText(company.getCompanyName())) {
            throw new SignUpException(ErrorCode.INVALID_COMPANY_NAME);
        }
    }
}
