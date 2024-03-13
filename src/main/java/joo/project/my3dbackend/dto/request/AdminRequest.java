package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.Address;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record AdminRequest(
        @NotBlank(message = "닉네임을 입력해주세요") String nickname,
        @NotBlank(message = "전화번호를 입력해주세요") @Length(min = 9, max = 11, message = "전화번호는 9 ~ 11자리를 가져야합니다") String phone,
        @NotBlank(message = "우편번호를 입력해주세요") String zipcode,
        @NotBlank(message = "주소를 입력해주세요") String street,
        @NotBlank(message = "상세 주소를 입력해주세요") String detail) {

    public Address getAddress() {
        return Address.of(zipcode, street, detail);
    }
}
