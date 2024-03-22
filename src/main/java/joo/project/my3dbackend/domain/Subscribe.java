package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.constants.PackageType;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(name = "subscribe")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Subscribe implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_subscribe", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private PackageType packageType;

    @Setter
    @Column(nullable = false)
    private SubscribeStatus subscribeStatus;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userAccountId", insertable = false, updatable = false)
    private UserAccount userAccount;

    @Column(nullable = false)
    private Long userAccountId;

    private Subscribe(
            PackageType packageType, SubscribeStatus subscribeStatus, LocalDateTime startedAt, Long userAccountId) {
        this.packageType = packageType;
        this.subscribeStatus = subscribeStatus;
        this.startedAt = startedAt;
        this.userAccountId = userAccountId;
    }

    public static Subscribe of(
            PackageType packageType, SubscribeStatus subscribeStatus, LocalDateTime startedAt, Long userAccountId) {
        return new Subscribe(packageType, subscribeStatus, startedAt, userAccountId);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
