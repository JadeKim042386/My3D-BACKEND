package joo.project.my3dbackend.domain.constants;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    COMPANY("ROLE_COMPANY"),
    ANONYMOUS("ROLE_ANONYMOUS");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }
}
