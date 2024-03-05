package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.ArticleComment;

import javax.validation.constraints.NotBlank;

public record ArticleCommentRequest(@NotBlank String content, Long parentCommentId) {
    public ArticleComment toEntity(Long userAccountId, Long articleId) {
        return ArticleComment.of(content, userAccountId, articleId, parentCommentId);
    }
}
