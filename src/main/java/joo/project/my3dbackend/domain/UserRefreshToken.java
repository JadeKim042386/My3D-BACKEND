package joo.project.my3dbackend.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(
        name = "user_refresh_token",
        indexes = {@Index(name = "reissue_count_idx", columnList = "reissueCount")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRefreshToken implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_user_refresh_token", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(mappedBy = "userRefreshToken", cascade = CascadeType.ALL)
    private UserAccount userAccount;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private long reissueCount = 0;

    private UserRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static UserRefreshToken of(String refreshToken) {
        return new UserRefreshToken(refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean equalRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void increaseReissueCount() {
        this.reissueCount++;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
