package joo.project.my3dbackend.config;

import joo.project.my3dbackend.domain.Address;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.security.SecurityConfig;
import joo.project.my3dbackend.security.TokenProvider;
import joo.project.my3dbackend.service.impl.UserAccountService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean
    private UserAccountService userAccountService;
    @MockBean
    private TokenProvider tokenProvider;

    @BeforeTestMethod
    void securitySetUp() {
        String userEmail = "testUser@gmail.com";
        given(userAccountService.getUserAccountByEmail(eq(userEmail))).willReturn(createUserAccount(userEmail));
    }

    private UserAccount createUserAccount(String email) {
        return Fixture.createUserAccount(
                email, "pw", "test", "01011112222", Address.of("12345", "street", "detail"), UserRole.USER);
    }
}
