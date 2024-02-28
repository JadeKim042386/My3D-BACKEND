package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.security.UserPrincipal;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        String title,
        String content,
        ArticleType articleType,
        ArticleCategory articleCategory,
        boolean isFree,
        String nickname,
        UserRole userRole,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {
    public static ArticleDto fromEntity(Article article, UserPrincipal userPrincipal) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getArticleType(),
                article.getArticleCategory(),
                article.isFree(),
                userPrincipal.nickname(),
                userPrincipal.getUserRole(),
                article.getCreatedAt(),
                article.getModifiedAt());
    }
}
