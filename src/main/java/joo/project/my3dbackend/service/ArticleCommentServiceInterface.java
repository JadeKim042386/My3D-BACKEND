package joo.project.my3dbackend.service;

import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;

public interface ArticleCommentServiceInterface {

    /**
     * 댓글 추가
     */
    ArticleCommentDto writeComment(ArticleCommentRequest articleCommentRequest, Long userAccountId, Long articleId);

    /**
     * 댓글 삭제
     */
    void deleteComment(Long articleCommentId);
}
