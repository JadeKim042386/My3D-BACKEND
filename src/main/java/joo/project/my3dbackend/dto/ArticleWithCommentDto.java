package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;

import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentDto(ArticleDto article, Set<ArticleCommentDto> articleComments) {
    public static ArticleWithCommentDto fromEntity(Article article) {
        return new ArticleWithCommentDto(
                ArticleDto.fromEntity(article),
                article.getArticleComments().stream()
                        .map(ArticleCommentDto::fromEntity)
                        .collect(Collectors.toUnmodifiableSet()));
    }
}
