package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.audit.AuditingAt;
import joo.project.my3dbackend.domain.constants.AlarmType;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(
        name = "alarm",
        indexes = {@Index(name = "receiver_idx", columnList = "receiverId")})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Alarm extends AuditingAt implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_alarm", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType alarmType;

    @Column(nullable = false)
    private Long targetId; // 댓글 id

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", insertable = false, updatable = false)
    private Article article;

    @Column(nullable = false)
    private Long articleId;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", insertable = false, updatable = false)
    private UserAccount sender; // 알람을 받는 유저

    @Column(nullable = false)
    private Long senderId;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId", insertable = false, updatable = false)
    private UserAccount receiver; // 알람을 받는 유저

    @Column(nullable = false)
    private Long receiverId;

    @Setter
    private LocalDateTime readAt; // 알람을 읽은 시간

    private Alarm(AlarmType alarmType, Long targetId, Long articleId, Long senderId, Long receiverId) {
        this.alarmType = alarmType;
        this.senderId = senderId;
        this.targetId = targetId;
        this.articleId = articleId;
        this.receiverId = receiverId;
    }

    public static Alarm of(AlarmType alarmType, Long targetId, Long articleId, Long senderId, Long receiverId) {
        return new Alarm(alarmType, targetId, articleId, senderId, receiverId);
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * 알람을 읽었는지에 대한 여부
     */
    public boolean isRead() {
        return !Objects.isNull(this.readAt);
    }
}
