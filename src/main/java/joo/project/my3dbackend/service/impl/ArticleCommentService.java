package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.ArticleCommentRepository;
import joo.project.my3dbackend.service.ArticleCommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService implements ArticleCommentServiceInterface {
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    @Override
    public ArticleComment retrieveComment(Long articleCommentId) {
        return articleCommentRepository
                .findById(articleCommentId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_COMMENT));
    }

    @Override
    public ArticleCommentDto writeComment(
            ArticleCommentRequest articleCommentRequest, UserPrincipal userPrincipal, Long articleId) {
        ArticleComment writedArticleComment = articleCommentRequest.toEntity(userPrincipal.id(), articleId);
        // parentCommentId가 null이면 부모 댓글, 아니면 자식 댓글
        articleCommentRequest
                .parentCommentId()
                .ifPresentOrElse(
                        parentCommentId -> {
                            ArticleComment parentComment = retrieveComment(parentCommentId);
                            parentComment.addChildComment(writedArticleComment);
                        },
                        () -> articleCommentRepository.save(writedArticleComment));
        articleCommentRepository.flush();

        return ArticleCommentDto.fromEntity(writedArticleComment, userPrincipal);
    }

    @Override
    public void deleteComment(Long articleCommentId) {
        // TODO: 댓글이 존재할 경우에 삭제할 수 있다
        articleCommentRepository.deleteById(articleCommentId);
    }
}
