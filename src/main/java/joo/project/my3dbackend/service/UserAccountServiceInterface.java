package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.dto.CompanyDto;
import joo.project.my3dbackend.dto.request.AdminRequest;
import joo.project.my3dbackend.dto.request.CompanyRequest;
import org.springframework.transaction.annotation.Transactional;

public interface UserAccountServiceInterface {

    /**
     * 이메일로 유저 정보 조회
     */
    UserAccount getUserAccountByEmail(String email);

    /**
     * ID로 유저 정보 조회
     */
    UserAccount getUserAccountById(Long userAccountId);

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
    void updatePassword(String email, String password);

    CompanyDto updateCompany(CompanyRequest companyRequest, Long userAccountId);

    boolean existsEmail(String email);

    boolean existsNickname(String nickname);

    /**
     * 유저 삭제
     */
    void deleteUser(Long userAccountId);
}
