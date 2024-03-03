package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.security.UserPrincipal;

public record UserInfo(String nickname, UserRole userRole) {
    public static UserInfo fromEntity(UserAccount userAccount) {
        return new UserInfo(userAccount.getNickname(), userAccount.getUserRole());
    }

    public static UserInfo fromPrincipal(UserPrincipal userPrincipal) {
        return new UserInfo(userPrincipal.nickname(), userPrincipal.getUserRole());
    }
}
