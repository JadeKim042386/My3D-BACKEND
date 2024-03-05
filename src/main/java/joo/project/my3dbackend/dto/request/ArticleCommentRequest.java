package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.ArticleComment;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public record ArticleCommentRequest(@NotBlank String content, Optional<Long> parentCommentId) {
    public ArticleComment toEntity(Long userAccountId, Long articleId) {
        return ArticleComment.of(content, userAccountId, articleId, parentCommentId.orElse(null));
    }
}
