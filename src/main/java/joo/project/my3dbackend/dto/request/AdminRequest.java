package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.Address;

import javax.validation.constraints.NotBlank;

public record AdminRequest(
        @NotBlank(message = "닉네임을 입력해주세요") String nickname,
        @NotBlank(message = "전화번호를 입력해주세요") String phone,
        @NotBlank(message = "우편번호를 입력해주세요") String zipcode,
        @NotBlank(message = "주소를 입력해주세요") String street,
        @NotBlank(message = "상세 주소를 입력해주세요") String detail) {

    public Address getAddress() {
        return Address.of(zipcode, street, detail);
    }
}
