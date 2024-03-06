package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.security.UserPrincipal;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id, String content, LocalDateTime createdAt, String nickname, Long parentCommentId) {

    public static ArticleCommentDto fromEntity(ArticleComment articleComment, UserPrincipal userPrincipal) {
        return new ArticleCommentDto(
                articleComment.getId(),
                articleComment.getContent(),
                articleComment.getCreatedAt(),
                userPrincipal.nickname(),
                articleComment.getParentCommentId());
    }

    public static ArticleCommentDto fromEntity(ArticleComment articleComment) {
        return new ArticleCommentDto(
                articleComment.getId(),
                articleComment.getContent(),
                articleComment.getCreatedAt(),
                articleComment.getUserAccount().getNickname(),
                articleComment.getParentCommentId());
    }
}
