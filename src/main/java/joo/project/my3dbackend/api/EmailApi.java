package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.EmailRequest;
import joo.project.my3dbackend.dto.response.EmailResponse;
import joo.project.my3dbackend.service.EmailServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class EmailApi {

    private final EmailServiceInterface emailService;

    /**
     * 회원가입시 이메일 존재 여부 확인을 위해 인증 코드 전송
     */
    @PostMapping("/send_code")
    public ResponseEntity<EmailResponse> sendEmailCertification(@RequestBody @Valid EmailRequest emailRequest) {
        // TODO: 이메일 중복 체크
        String subject = "[My3D] 이메일 인증";
        String code = generateEmailCode(); // 인증 코드
        emailService.sendEmail(emailRequest.email(), subject, code);

        return ResponseEntity.ok(EmailResponse.sendSuccess(emailRequest.email(), code));
    }

    private String generateEmailCode() {
        return String.valueOf(Math.round(Math.random() * 10000));
    }
}
