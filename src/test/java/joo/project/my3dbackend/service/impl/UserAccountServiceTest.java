package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {
    @InjectMocks
    private UserAccountService userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("이메일로 유저 조회")
    @Test
    void getUserAccountByEmail() {
        // given
        UserAccount userAccount = Fixture.createUserAccount();
        given(userAccountRepository.findByEmail(anyString())).willReturn(Optional.of(userAccount));
        // when
        userAccountService.getUserAccountByEmail(userAccount.getEmail());
        // then
    }

    @DisplayName("유저 정보 수정")
    @ParameterizedTest
    @CsvSource(
            value = {
                "admin, 01011112222, 54321, street2, detail2",
                "admin, 01011112222, 12345, street, detail",
                "admin, 01012341234, 12345, street, detail"
            })
    void updateUser(String nickname, String phone, String zipcode, String street, String detail) {
        // given
        String email = "test@gmail.com";
        AdminRequest adminRequest = FixtureDto.createAdminRequest(nickname, phone, zipcode, street, detail);
        given(userAccountRepository.findByEmail(anyString())).willReturn(Optional.of(Fixture.createUserAccount()));
        // when
        userAccountService.updateUser(email, adminRequest);
        // then
    }

    @DisplayName("비밀번호 수정")
    @Test
    void updatePassword() {
        // given
        String email = "test@gmail.com";
        PasswordRequest passwordRequest = FixtureDto.createPasswordRequest();
        given(userAccountRepository.findByEmail(anyString())).willReturn(Optional.of(Fixture.createUserAccount()));
        // when
        userAccountService.updatePassword(email, passwordRequest);
        // then
    }
}