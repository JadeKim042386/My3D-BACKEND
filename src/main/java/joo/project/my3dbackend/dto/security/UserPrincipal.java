package joo.project.my3dbackend.dto.security;

import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public record UserPrincipal(
        Long id, String email, String password, Collection<? extends GrantedAuthority> authorities, String nickname)
        implements UserDetails {

    public static UserPrincipal of(Long id, String email, String password, String nickname, UserRole userRole) {
        return new UserPrincipal(id, email, password, Set.of(new SimpleGrantedAuthority(userRole.getName())), nickname);
    }

    public static UserPrincipal fromEntity(UserAccount userAccount) {
        return UserPrincipal.of(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getPassword(),
                userAccount.getNickname(),
                userAccount.getUserRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
