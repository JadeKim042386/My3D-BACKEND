package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.SignUpRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/signup")
@RequiredArgsConstructor
public class SignUpApi {

    private final UserAccountServiceInterface userAccountService;

    /**
     * 회원가입 요청
     */
    @PostMapping
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
        // TODO: 이메일, 닉네임 중복체크
        // TODO: 기업 회원가입
        userAccountService.saveUser(signUpRequest.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("you're successfully sign up. you can be login."));
    }
}
