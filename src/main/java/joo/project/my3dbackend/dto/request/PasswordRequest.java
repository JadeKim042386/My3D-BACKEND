package joo.project.my3dbackend.dto.request;

import javax.validation.constraints.Pattern;

public record PasswordRequest(
        @Pattern(
                regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = "비밀번호는 영문자,숫자,특수문자 포함 최소 8~20자를 사용하세요")
        String password
) {
}
