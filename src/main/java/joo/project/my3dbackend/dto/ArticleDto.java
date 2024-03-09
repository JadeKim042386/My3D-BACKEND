package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.dto.security.UserPrincipal;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        String title,
        String content,
        ArticleType articleType,
        ArticleCategory articleCategory,
        boolean isFree,
        int likeCount,
        UserInfo userInfo,
        ArticleFileDto articleFile,
        DimensionOptionDto dimensionOption,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {

    public static ArticleDto of(Article article, UserInfo userInfo) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getArticleType(),
                article.getArticleCategory(),
                article.isFree(),
                article.getLikeCount(),
                userInfo,
                ArticleFileDto.fromEntity(article.getArticleFile()),
                DimensionOptionDto.fromEntity(article.getArticleFile().getDimensionOption()),
                article.getCreatedAt(),
                article.getModifiedAt());
    }

    public static ArticleDto fromEntity(Article article, UserPrincipal userPrincipal) {
        return ArticleDto.of(article, UserInfo.fromPrincipal(userPrincipal));
    }

    public static ArticleDto fromEntity(Article article) {
        return ArticleDto.of(article, UserInfo.fromEntity(article.getUserAccount()));
    }
}
