package joo.project.my3dbackend.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(name = "verify_email")
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VerifyEmail implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_verify_email", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String secretCode;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private VerifyEmail(String email, String secretCode, LocalDateTime expiredAt) {
        this.email = email;
        this.secretCode = secretCode;
        this.expiredAt = expiredAt;
    }

    public static VerifyEmail of(String email, String secretCode, LocalDateTime expiredAt) {
        return new VerifyEmail(email, secretCode, expiredAt);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
