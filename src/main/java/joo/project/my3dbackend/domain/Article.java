package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.audit.AuditingAt;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "article")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Article extends AuditingAt implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_article", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleType articleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleCategory articleCategory;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userAccountId")
    private UserAccount userAccount;

    // TODO: articleFile과 연관 관계 설정
    // TODO: articleComment와 연관 관계 설정
    // TODO: articleLike와 연관 관계 설정
    // TODO: alarm과 연관 관계 설정

    /**
     * true일 경우 무료 게시글, false일 경우 유료 게시글
     */
    @Column(nullable = false)
    private boolean isFree;

    private Article(
            String title, String content, ArticleType articleType, ArticleCategory articleCategory, boolean isFree) {
        this.title = title;
        this.content = content;
        this.articleType = articleType;
        this.articleCategory = articleCategory;
        this.isFree = isFree;
        // TODO: userAccount 추가
    }

    public static Article of(
            String title, String content, ArticleType articleType, ArticleCategory articleCategory, boolean isFree) {
        return new Article(title, content, articleType, articleCategory, isFree);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
