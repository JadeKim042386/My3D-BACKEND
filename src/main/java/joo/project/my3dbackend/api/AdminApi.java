package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminApi {
    private final UserAccountServiceInterface userAccountService;

    /**
     * 사용자 정보 수정 요청 (닉네임, 전화번호, 주소)
     */
    @PutMapping
    public ResponseEntity<ApiResponse> updateUserData(@RequestBody @Valid AdminRequest adminRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userAccountService.updateUser(userPrincipal.email(), adminRequest);

        return ResponseEntity.ok(ApiResponse.of("You successfully updated user account"));
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse> updatePassword(@Valid PasswordRequest passwordRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userAccountService.updatePassword(userPrincipal.email(), passwordRequest);

        return ResponseEntity.ok(ApiResponse.of("You successfully updated password"));
    }
}
