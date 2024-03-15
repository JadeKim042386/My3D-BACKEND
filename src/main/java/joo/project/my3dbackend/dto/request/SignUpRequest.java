package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.Address;
import joo.project.my3dbackend.domain.Company;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.UserRole;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank(message = "이메일이 입력되지 않았습니다") @Email(message = "이메일 형식이 잘못되었습니다") String email,
        @NotNull(message = "회원 유형을 찾을 수 없습니다") UserRole userRole,
        @NotBlank(message = "닉네임을 입력해주세요") String nickname,
        @Pattern(
                        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                        message = "비밀번호는 영문자,숫자,특수문자 포함 최소 8~20자를 사용하세요")
                String password,
        @NotBlank(message = "전화번호를 입력해주세요") @Length(min = 9, max = 11, message = "전화번호는 9 ~ 11자리를 가져야합니다") String phone,
        @NotBlank(message = "우편번호를 입력해주세요") String zipcode,
        @NotBlank(message = "주소를 입력해주세요") String street,
        @NotBlank(message = "상세 주소를 입력해주세요") String detail,
        String companyName) {

    public UserAccount toEntity() {
        if (userRole.equals(UserRole.COMPANY)) {
            return UserAccount.ofCompanyUser(
                    email,
                    password,
                    nickname,
                    phone,
                    Address.of(zipcode, street, detail),
                    userRole,
                    Company.of(companyName, null));
        } else {
            return UserAccount.ofGeneralUser(
                    email, password, nickname, phone, Address.of(zipcode, street, detail), userRole);
        }
    }
}
