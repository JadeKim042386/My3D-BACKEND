package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.EmailRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.response.EmailResponse;
import joo.project.my3dbackend.exception.AuthException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.security.PasswordGenerator;
import joo.project.my3dbackend.service.EmailServiceInterface;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class EmailApi {
    private final PasswordGenerator passwordGenerator;
    private final EmailServiceInterface emailService;
    private final UserAccountServiceInterface userAccountService;

    /**
     * 회원가입시 이메일 존재 여부 확인을 위해 인증 코드 전송
     */
    @PostMapping("/send-code")
    public ResponseEntity<EmailResponse> sendEmailCertification(@RequestBody @Valid EmailRequest emailRequest) {
        checkIfDuplicatedEmail(emailRequest.email());
        String subject = "[My3D] 이메일 인증";
        String code = generateEmailCode(); // 인증 코드
        emailService.sendAsyncEmail(emailRequest.email(), subject, code);

        return ResponseEntity.ok(EmailResponse.sendSuccess(emailRequest.email(), code));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyEmail(@RequestBody @Valid EmailRequest emailRequest) {
        return ResponseEntity.ok(ApiResponse.of(emailService.verifyEmail(emailRequest.email(), emailRequest.code())));
    }

    /**
     * 임의 비밀번호 생성 후 전송
     */
    @PostMapping("/find-pass")
    public ResponseEntity<ApiResponse<String>> sendRandomPassword(@RequestBody @Valid EmailRequest emailRequest) {
        checkIfExistsUser(emailRequest.email());
        String subject = "[My3D] 임시 비밀번호";
        String password = generatePassword();
        emailService.sendRandomPassword(emailRequest.email(), subject, password);
        return ResponseEntity.ok(ApiResponse.of("successfully send password"));
    }

    /**
     * 메일 전송 여부 확인
     * 전송 여부를 확인할지는 사용자가 선택
     */
    @PostMapping("/check-sent")
    public ResponseEntity<ApiResponse<Boolean>> isCompleteSentEmail(@RequestBody @Valid EmailRequest emailRequest) {

        return ResponseEntity.ok(ApiResponse.of(emailService.isCompleteSentEmail(emailRequest.email())));
    }

    /**
     * 6자리 인증 코드 생성
     */
    private String generateEmailCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * 임의 비밀번호 생성
     */
    private String generatePassword() {
        return passwordGenerator.generateRandomPassword();
    }

    /**
     * 이메일 중복 체크
     */
    private void checkIfDuplicatedEmail(String email) {
        if (userAccountService.existsEmail(email)) throw new AuthException(ErrorCode.ALREADY_EXIST_EMAIL);
    }

    /**
     * 회원가입 여부 확인
     */
    private void checkIfExistsUser(String email) {
        if (!userAccountService.existsEmail(email)) throw new AuthException(ErrorCode.NOT_FOUND_USER);
    }
}
