package joo.project.my3dbackend.config;

import joo.project.my3dbackend.security.SecurityConfig;
import joo.project.my3dbackend.service.impl.UserAccountService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserAccountService userAccountService;
}
