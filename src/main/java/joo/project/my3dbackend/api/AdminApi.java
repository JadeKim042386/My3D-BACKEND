package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.CompanyDto;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.CompanyRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.UserAccountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<String>> updateUserData(
            @RequestBody @Valid AdminRequest adminRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userAccountService.updateUser(userPrincipal.email(), adminRequest);
        return ResponseEntity.ok(ApiResponse.of("You successfully updated user account"));
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @Valid PasswordRequest passwordRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userAccountService.updatePassword(userPrincipal.email(), passwordRequest.password());
        return ResponseEntity.ok(ApiResponse.of("You successfully updated password"));
    }

    /**
     * 기업 정보 수정 요청
     */
    @PutMapping("/company")
    public ResponseEntity<CompanyDto> updateCompany(
            @RequestBody @Valid CompanyRequest companyAdminRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // TODO: 기업 유저가 맞는지 확인
        return ResponseEntity.ok(userAccountService.updateCompany(companyAdminRequest, userPrincipal.id()));
    }

    /**
     * 유저 삭제 (회원 탈퇴)
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        // TODO: 게시글, 댓글, 알람 등 bulk delete
        userAccountService.deleteUser(userPrincipal.id());
        return ResponseEntity.ok(ApiResponse.of("You successfully delete user"));
    }
}
