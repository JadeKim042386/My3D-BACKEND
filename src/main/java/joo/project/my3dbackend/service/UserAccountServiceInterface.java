package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.UserAccount;

public interface UserAccountServiceInterface {

    /**
     * 유저 정보 조회
     */
    UserAccount getUserAccountByEmail(String email);
}
