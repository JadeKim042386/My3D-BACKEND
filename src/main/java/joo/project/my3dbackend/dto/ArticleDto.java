package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;

public record ArticleDto(
        Long id,
        String title,
        String content,
        ArticleType articleType,
        ArticleCategory articleCategory,
        boolean isFree) {
    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getArticleType(),
                article.getArticleCategory(),
                article.isFree());
    }
}
