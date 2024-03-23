package joo.project.my3dbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import joo.project.my3dbackend.config.TestSecurityConfig;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.request.SignUpRequest;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.service.impl.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(SignUpApi.class)
class SignUpApiTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserAccountService userAccountService;

    @MockBean
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("일반 유저 회원가입")
    @Test
    void signup_general() throws Exception {
        // given
        SignUpRequest signUpRequest = FixtureDto.createSignUpRequest();
        willDoNothing().given(userAccountService).registerUser(any(UserAccount.class));
        // when
        mvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());
        // then
    }

    @DisplayName("기업 유저 회원가입")
    @Test
    void signup_company() throws Exception {
        // given
        SignUpRequest signUpRequest = FixtureDto.createCompanySignUpRequest();
        willDoNothing().given(userAccountService).registerUser(any(UserAccount.class));
        // when
        mvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());
        // then
    }

    @DisplayName("회원가입 validation error")
    @ParameterizedTest
    @CsvSource(
            value = {
                ", USER, test, test1234@@, 01011112222, 12345, street, detail, ",
                "testgmail, USER, test, test1234@@, 01011112222, 12345, street, detail, ",
                "test@gmail, , test, test1234@@, 01011112222, 12345, street, detail, ",
                "test@gmail, USER, , test1234@@, 01011112222, 12345, street, detail, ",
                "test@gmail, USER, test, test1234, 01011112222, 12345, street, detail, ",
                "test@gmail, USER, test, test1234@@, 01011, 12345, street, detail, ",
                "test@gmail, USER, test, test1234@@, 01011112222, , street, detail, ",
                "test@gmail, USER, test, test1234@@, 01011112222, 12345, , detail, ",
                "test@gmail, USER, test, test1234@@, 01011112222, 12345, street, , ",
                "test@gmail, COMPANY, test, test1234@@, 01011112222, 12345, street, detail, ''"
            })
    void signup_validationError(
            String email,
            UserRole userRole,
            String nickname,
            String password,
            String phone,
            String zipcode,
            String street,
            String detail,
            String companyName)
            throws Exception {
        // given
        SignUpRequest signUpRequest = FixtureDto.createSignUpRequest(
                email, userRole, nickname, password, phone, zipcode, street, detail, companyName);
        // when
        mvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest());
        // then
    }
}