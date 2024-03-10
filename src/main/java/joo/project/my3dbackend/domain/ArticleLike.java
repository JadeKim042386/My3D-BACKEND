package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.audit.AuditingAt;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Getter
@ToString(callSuper = true)
@Table(name = "article_like")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArticleLike extends AuditingAt implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_article_like", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userAccountId", insertable = false, updatable = false)
    private UserAccount userAccount;

    @Column(nullable = false)
    private Long userAccountId;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", insertable = false, updatable = false)
    private Article article;

    @Column(nullable = false)
    private Long articleId;

    private ArticleLike(Long userAccountId, Long articleId) {
        this.userAccountId = userAccountId;
        this.articleId = articleId;
    }

    public static ArticleLike of(Long userAccountId, Long articleId) {
        return new ArticleLike(userAccountId, articleId);
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
