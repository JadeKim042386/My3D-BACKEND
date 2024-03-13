package joo.project.my3dbackend.security;

import joo.project.my3dbackend.domain.constants.UserRole;

public record TokenInfo(Long id, String email, String nickname, UserRole userRole) {
    public static TokenInfo of(String email, String nickname, String spec) {
        String[] sepcs = spec.split(":");
        return new TokenInfo(Long.parseLong(sepcs[0]), email, nickname, UserRole.valueOf(sepcs[1].substring(5)));
    }

    public static TokenInfo ofAnonymous() {
        return new TokenInfo(null, "ANONYMOUS", "ANONYMOUS", UserRole.ANONYMOUS);
    }
}
