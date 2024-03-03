package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.ArticleComment;

public record ArticleCommentDto(
        String content
) {
    public static ArticleCommentDto fromEntity(ArticleComment articleComment) {
        return new ArticleCommentDto(articleComment.getContent());
    }
}
