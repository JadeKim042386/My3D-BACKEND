package joo.project.my3dbackend.domain;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import joo.project.my3dbackend.domain.audit.AuditingAt;
import joo.project.my3dbackend.domain.constants.UserRole;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(
        name = "user_account",
        indexes = {
            @Index(name = "email_idx", columnList = "email", unique = true),
            @Index(name = "nickname_idx", columnList = "nickname", unique = true)
        })
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAccount extends AuditingAt implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_user_account", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(length = 11)
    private String phone;

    @Setter
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole userRole;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Article> articles = new LinkedHashSet<>();

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ArticleLike> articleLikes = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "companyId")
    private Company company;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private final Set<Alarm> senderAlarms = new LinkedHashSet<>();

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private final Set<Alarm> receiverAlarms = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Subscribe subscribe;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userRefreshTokenId")
    private UserRefreshToken userRefreshToken;

    public UserAccount(
            String email,
            String password,
            String nickname,
            String phone,
            Address address,
            UserRole userRole,
            Company company,
            UserRefreshToken userRefreshToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.userRole = userRole;
        this.company = company;
        this.userRefreshToken = userRefreshToken;
    }

    /**
     * 일반 유저 생성
     */
    public static UserAccount ofGeneralUser(
            String email,
            String password,
            String nickname,
            String phone,
            Address address,
            UserRole userRole,
            UserRefreshToken userRefreshToken) {
        return new UserAccount(email, password, nickname, phone, address, userRole, null, userRefreshToken);
    }

    /**
     * 기업 유저 생성
     */
    public static UserAccount ofCompanyUser(
            String email,
            String password,
            String nickname,
            String phone,
            Address address,
            UserRole userRole,
            Company company,
            UserRefreshToken userRefreshToken) {
        return new UserAccount(email, password, nickname, phone, address, userRole, company, userRefreshToken);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
