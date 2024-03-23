package joo.project.my3dbackend.security;

import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.security.UserPrincipal;

public record TokenInfo(Long id, String email, String nickname, UserRole userRole, SubscribeStatus subscribeStatus) {
    public static TokenInfo of(String email, String nickname, String spec, String subscribeStatus) {
        String[] sepcs = spec.split(":");
        return new TokenInfo(
                Long.parseLong(sepcs[0]),
                email,
                nickname,
                UserRole.valueOf(sepcs[1].substring(5)),
                SubscribeStatus.valueOf(subscribeStatus));
    }

    public static TokenInfo ofAnonymous() {
        return new TokenInfo(null, "ANONYMOUS", "ANONYMOUS", UserRole.ANONYMOUS, SubscribeStatus.STOP);
    }

    public UserPrincipal toUserPrincipal() {
        return UserPrincipal.of(id, email, nickname, userRole, subscribeStatus);
    }

    public String getSpec() {
        return String.format("%s:%s", id, userRole.getName());
    }
}
