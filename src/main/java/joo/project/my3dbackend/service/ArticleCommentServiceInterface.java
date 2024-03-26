package joo.project.my3dbackend.service;

import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCommentServiceInterface {

    /**
     * 대댓글 목록 조회
     */
    Page<ArticleCommentDto> getComments(Pageable pageable, Long parentCommentId);

    /**
     * 댓글 추가
     */
    ArticleCommentDto writeComment(
            ArticleCommentRequest articleCommentRequest, UserPrincipal userPrincipal, Long articleId);

    /**
     * 댓글 삭제
     */
    void deleteComment(Long articleCommentId);

    /**
     * 댓글 작성자 확인
     */
    boolean isWriterOfComment(Long userAccountId, Long articleCommentId);

    /**
     * 댓글이 존재하는지 확인
     */
    boolean existsComment(Long articleCommentId);
}
