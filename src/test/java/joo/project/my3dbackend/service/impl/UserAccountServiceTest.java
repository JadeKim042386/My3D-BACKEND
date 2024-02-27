package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @InjectMocks private UserAccountService userAccountService;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("이메일로 유저 조회")
    @Test
    void getUserAccountByEmail() {
        //given
        UserAccount userAccount = Fixture.createUserAccount();
        given(userAccountRepository.findByEmail(anyString())).willReturn(Optional.of(userAccount));
        //when
        userAccountService.getUserAccountByEmail(userAccount.getEmail());
        //then
    }

}