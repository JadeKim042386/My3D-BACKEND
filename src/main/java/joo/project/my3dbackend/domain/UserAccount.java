package joo.project.my3dbackend.domain;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import joo.project.my3dbackend.domain.constants.UserRole;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Table(name = "user_account")
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAccount implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_user_account", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(length = 11)
    private String phone;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole userRole;

    // TODO: order by created_at
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Set<Article> articles = new LinkedHashSet<>();

    // TODO: company와 연관 관계 설정
    // TODO: userRefreshToken과 연관 관계 설정
    // TODO: Auditing Field 추가

    public UserAccount(
            String email, String password, String nickname, String phone, Address address, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.userRole = userRole;
    }

    public static UserAccount of(
            String email, String password, String nickname, String phone, Address address, UserRole userRole) {
        return new UserAccount(email, password, nickname, phone, address, userRole);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}