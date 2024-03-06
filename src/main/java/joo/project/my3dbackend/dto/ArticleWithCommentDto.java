package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;

import java.util.List;

public record ArticleWithCommentDto(ArticleDto article, List<ArticleCommentDto> articleComments) {
    public static ArticleWithCommentDto fromEntity(Article article) {
        return new ArticleWithCommentDto(
                ArticleDto.fromEntity(article),
                article.getArticleComments().stream()
                        .map(ArticleCommentDto::fromEntity)
                        .toList());
    }
}
