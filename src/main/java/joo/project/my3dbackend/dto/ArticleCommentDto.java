package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.security.UserPrincipal;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record ArticleCommentDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        String nickname,
        Long parentCommentId,
        Set<ArticleCommentDto> childComments) {

    public static ArticleCommentDto of(
            Long id, String content, LocalDateTime createdAt, String nickname, Long parentCommentId) {
        return new ArticleCommentDto(
                id,
                content,
                createdAt,
                nickname,
                parentCommentId,
                new TreeSet<>(Comparator.comparing(ArticleCommentDto::createdAt).thenComparing(ArticleCommentDto::id)));
    }

    public static ArticleCommentDto fromEntity(ArticleComment articleComment, UserPrincipal userPrincipal) {
        return ArticleCommentDto.of(
                articleComment.getId(),
                articleComment.getContent(),
                articleComment.getCreatedAt(),
                userPrincipal.nickname(),
                articleComment.getParentCommentId());
    }

    public static ArticleCommentDto fromEntity(ArticleComment articleComment) {
        return ArticleCommentDto.of(
                articleComment.getId(),
                articleComment.getContent(),
                articleComment.getCreatedAt(),
                articleComment.getUserAccount().getNickname(),
                articleComment.getParentCommentId());
    }
}
