package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.UserAccount;

public interface UserAccountServiceInterface {

    /**
     * 이메일로 유저 정보 조회
     */
    UserAccount getUserAccountByEmail(String email);

    /**
     * 회원가입
     */
    void saveUser(UserAccount userAccount);
}
