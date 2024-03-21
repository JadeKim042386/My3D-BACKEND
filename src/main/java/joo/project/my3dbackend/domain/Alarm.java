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
    @JoinColumn(name = "articleId")
    private Article article;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private UserAccount sender; // 알람을 받는 유저

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId")
    private UserAccount receiver; // 알람을 받는 유저

    @Setter
    private LocalDateTime readAt; // 알람을 읽은 시간

    private Alarm(AlarmType alarmType, Long targetId, Article article, UserAccount sender, UserAccount receiver) {
        this.alarmType = alarmType;
        this.sender = sender;
        this.targetId = targetId;
        this.article = article;
        this.receiver = receiver;
    }

    public static Alarm of(
            AlarmType alarmType, Long targetId, Article article, UserAccount sender, UserAccount receiver) {
        return new Alarm(alarmType, targetId, article, sender, receiver);
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
