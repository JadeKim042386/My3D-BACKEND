package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.audit.AuditingAt;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "article_comment")
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

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userAccountId")
    private UserAccount userAccount;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article article;

    public ArticleComment(String content, UserAccount userAccount, Article article) {
        this.content = content;
        this.userAccount = userAccount;
        this.article = article;
    }

    public static ArticleComment of(String content, UserAccount userAccount, Article article) {
        return new ArticleComment(content, userAccount, article);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
