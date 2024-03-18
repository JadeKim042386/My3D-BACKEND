package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.audit.AuditingAt;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "article_comment", indexes = @Index(name = "parent_comment_id_idx", columnList = "parentCommentId"))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArticleComment extends AuditingAt implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_article_comment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(updatable = false)
    private Long parentCommentId;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userAccountId", insertable = false, updatable = false)
    private UserAccount userAccount;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", insertable = false, updatable = false)
    private Article article;

    @Column(nullable = false)
    private Long userAccountId;

    @Column(nullable = false)
    private Long articleId;

    public ArticleComment(String content, Long userAccountId, Long articleId, Long parentCommentId) {
        this.content = content;
        this.userAccountId = userAccountId;
        this.articleId = articleId;
        this.parentCommentId = parentCommentId;
    }

    public static ArticleComment of(String content, Long userAccountId, Long articleId, Long parentCommentId) {
        return new ArticleComment(content, userAccountId, articleId, parentCommentId);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
