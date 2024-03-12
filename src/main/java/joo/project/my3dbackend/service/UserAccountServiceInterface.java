package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.PasswordRequest;

public interface UserAccountServiceInterface {

    /**
     * 이메일로 유저 정보 조회
     */
    UserAccount getUserAccountByEmail(String email);

    /**
     * 회원가입
     */
    void registerUser(UserAccount userAccount);

    /**
     * 유저 정보 수정
     */
    void updateUser(String email, AdminRequest adminRequest);

    /**
     * 비밀번호 변경
     */
    void updatePassword(String email, PasswordRequest passwordRequest);
}
