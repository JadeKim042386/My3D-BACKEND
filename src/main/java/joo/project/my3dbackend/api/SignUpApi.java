package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.SignUpRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.security.TokenProvider;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final TokenProvider tokenProvider;

    /**
     * 회원가입 요청
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
        checkIfDuplicatedEmailOrNickname(signUpRequest.email(), signUpRequest.nickname());
        userAccountService.registerUser(signUpRequest.toEntity(tokenProvider.generateRefreshToken()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("you're successfully sign up. you can be login."));
    }

    /**
     * 이메일, 닉네임 중복 확인
     */
    private void checkIfDuplicatedEmailOrNickname(String email, String nickname) {
        if (userAccountService.existsEmail(email)) throw new AuthException(ErrorCode.ALREADY_EXIST_EMAIL);
        if (userAccountService.existsNickname(nickname)) throw new AuthException(ErrorCode.ALREADY_EXIST_NICKNAME);
    }
}
