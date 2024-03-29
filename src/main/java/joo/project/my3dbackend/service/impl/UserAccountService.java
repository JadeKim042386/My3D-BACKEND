package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Address;
import joo.project.my3dbackend.domain.Company;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.dto.CompanyDto;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.CompanyRequest;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.SignUpException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.UserAccountRepository;
import joo.project.my3dbackend.service.SubscribeServiceInterface;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService implements UserAccountServiceInterface {
    private final PasswordEncoder encoder;
    private final UserAccountRepository userAccountRepository;
    private final SubscribeServiceInterface subscribeService;

    @Transactional(readOnly = true)
    @Override
    public UserAccount getUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email).orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getUserAccountById(Long userAccountId) {
        return userAccountRepository
                .findById(userAccountId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public void registerUser(UserAccount userAccount) {
        checkIfValidCompany(userAccount.getCompany());
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccountRepository.save(userAccount);
        // 회원가입시 기본으로 무료 구독으로 등록
        subscribeService.saveSubscribe(PackageType.FREE, SubscribeStatus.STOP, userAccount.getId());
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
    public void updatePassword(String email, String password) {
        UserAccount userAccount = getUserAccountByEmail(email);
        userAccount.setPassword(encodePassword(password));
    }

    @Override
    public CompanyDto updateCompany(CompanyRequest companyRequest, Long userAccountId) {
        UserAccount userAccount = getUserAccountById(userAccountId);
        Optional.ofNullable(companyRequest.companyName()).ifPresent(userAccount.getCompany()::setCompanyName);
        Optional.ofNullable(companyRequest.homepage()).ifPresent(userAccount.getCompany()::setHomepage);
        return CompanyDto.fromEntity(userAccount.getCompany());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsEmail(String email) {
        return userAccountRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsNickname(String nickname) {
        return userAccountRepository.existsByNickname(nickname);
    }

    public void deleteUser(Long userAccountId) {
        userAccountRepository.deleteById(userAccountId);
    }

    /**
     * 비밀번호 인코딩
     */
    private String encodePassword(String password) {
        return encoder.encode(password);
    }

    /**
     * 유저 정보 업데이트 여부 확인
     */
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
