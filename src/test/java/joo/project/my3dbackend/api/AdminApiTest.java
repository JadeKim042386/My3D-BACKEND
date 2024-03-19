package joo.project.my3dbackend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import joo.project.my3dbackend.config.TestSecurityConfig;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.CompanyRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.service.impl.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(AdminApi.class)
class AdminApiTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("유저 정보 수정")
    @Test
    void updateUserData() throws Exception {
        // given
        AdminRequest adminRequest = FixtureDto.createAdminRequest();
        willDoNothing().given(userAccountService).updateUser(anyString(), any(AdminRequest.class));
        // when
        mvc.perform(put("/api/v1/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRequest)))
                .andExpect(status().isOk());
        // then
    }

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("비밀번호 수정")
    @Test
    void updatePassword() throws Exception {
        // given
        PasswordRequest passwordRequest = FixtureDto.createPasswordRequest();
        willDoNothing().given(userAccountService).updateUser(anyString(), any(AdminRequest.class));
        // when
        mvc.perform(put("/api/v1/admin/password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isOk());
        // then
    }

    @WithUserDetails(value = "testCompany@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("기업 정보 수정")
    @Test
    void updateCompany() throws Exception {
        // given
        CompanyRequest companyRequest = FixtureDto.createCompanyRequest();
        given(userAccountService.updateCompany(any(CompanyRequest.class), anyLong()))
                .willReturn(FixtureDto.createCompanyDto(companyRequest.companyName(), companyRequest.homepage()));
        // when
        mvc.perform(put("/api/v1/admin/company").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value(companyRequest.companyName()))
                .andExpect(jsonPath("$.homepage").value(companyRequest.homepage()));
        // then
    }
}
