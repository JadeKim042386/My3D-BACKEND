package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.ArticleFile;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.dto.security.UserPrincipal;

import java.time.LocalDateTime;
import java.util.Optional;

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
                getArticleFileFromEntity(article.getArticleFile()),
                getDimensionOptionFromEntity(article.getArticleFile()),
                article.getCreatedAt(),
                article.getModifiedAt());
    }

    public static ArticleDto fromEntity(Article article, UserPrincipal userPrincipal) {
        return ArticleDto.of(article, UserInfo.fromPrincipal(userPrincipal));
    }

    public static ArticleDto fromEntity(Article article) {
        return ArticleDto.of(article, UserInfo.fromEntity(article.getUserAccount()));
    }

    /**
     * 파일이 없을 경우 null 반환
     */
    private static ArticleFileDto getArticleFileFromEntity(ArticleFile articleFile) {
        return Optional.ofNullable(articleFile).map(ArticleFileDto::fromEntity).orElse(null);
    }

    /**
     * 파일이 없을 경우 null 반환
     */
    public static DimensionOptionDto getDimensionOptionFromEntity(ArticleFile articleFile) {
        return Optional.ofNullable(articleFile)
                .map(ArticleFile::getDimensionOption)
                .map(DimensionOptionDto::fromEntity)
                .orElse(null);
    }
}
