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

import java.util.Objects;

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
        // parentCommentId가 null 값을 가질 경우 부모 댓글, 값을 가질 경우 자식 댓글에 해당
        ArticleComment writedArticleComment = articleCommentRequest.toEntity(userPrincipal.id(), articleId);
        if (Objects.isNull(articleCommentRequest.parentCommentId())) {
            articleCommentRepository.save(writedArticleComment);
        } else {
            ArticleComment parentComment = retrieveComment(articleCommentRequest.parentCommentId());
            parentComment.addChildComment(writedArticleComment);
        }
        articleCommentRepository.flush();

        return ArticleCommentDto.fromEntity(writedArticleComment, userPrincipal);
    }

    @Override
    public void deleteComment(Long articleCommentId) {
        // TODO: 댓글이 존재할 경우에 삭제할 수 있다
        articleCommentRepository.deleteById(articleCommentId);
    }
}
