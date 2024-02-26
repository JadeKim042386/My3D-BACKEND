package joo.project.my3dbackend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String zipcode; // 우편번호
    private String street; // 주소
    private String detail; // 상세주소

    private Address(String zipcode, String street, String detail) {
        this.zipcode = zipcode;
        this.street = street;
        this.detail = detail;
    }

    public static Address of(String zipcode, String street, String detail) {
        return new Address(zipcode, street, detail);
    }
}
