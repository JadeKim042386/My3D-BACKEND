package joo.project.my3dbackend.security;

import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf()
                .disable()
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        .anyRequest()
                        .permitAll())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountServiceInterface userAccountService) {
        return email -> UserPrincipal.fromEntity(userAccountService.getUserAccountByEmail(email));
    }

    @Bean
    public BCryptPasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }
}
