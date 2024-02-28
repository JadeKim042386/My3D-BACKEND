package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.UserAccount;

public interface UserAccountServiceInterface {

    /**
     * 이메일로 유저 정보 조회
     */
    UserAccount getUserAccountByEmail(String email);

    /**
     * id로 유저 reference 조회
     */
    UserAccount getReferenceUserAccountById(Long userAccountId);
}
