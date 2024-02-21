package joo.project.my3dbackend.domain;

import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "article")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Article {
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

    public static Article of(String title, String content, ArticleType articleType, ArticleCategory articleCategory) {
        return new Article(title, content, articleType, articleCategory);
    }

    private Article(String title, String content, ArticleType articleType, ArticleCategory articleCategory) {
        this.title = title;
        this.content = content;
        this.articleType = articleType;
        this.articleCategory = articleCategory;
    }
}
