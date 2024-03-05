package joo.project.my3dbackend.service;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;

public interface ArticleCommentServiceInterface {

    /**
     * 댓글 조회
     */
    ArticleComment retrieveComment(Long articleCommentId);

    /**
     * 댓글 추가
     */
    ArticleCommentDto writeComment(ArticleCommentRequest articleCommentRequest, UserPrincipal userPrincipal, Long articleId);

    /**
     * 댓글 삭제
     */
    void deleteComment(Long articleCommentId);
}
