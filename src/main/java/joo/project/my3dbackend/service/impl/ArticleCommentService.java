package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.repository.ArticleCommentRepository;
import joo.project.my3dbackend.service.ArticleCommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService implements ArticleCommentServiceInterface {
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<ArticleCommentDto> getChildComments(Pageable pageable, Long parentCommentId) {
        return articleCommentRepository.findAllChildComments(pageable, parentCommentId).map(ArticleCommentDto::fromEntity);
    }

    @Override
    public ArticleCommentDto writeComment(
            ArticleCommentRequest articleCommentRequest, UserPrincipal userPrincipal, Long articleId) {

        ArticleComment savedArticleComment =
                articleCommentRepository.save(articleCommentRequest.toEntity(userPrincipal.id(), articleId));
        return ArticleCommentDto.fromEntity(savedArticleComment, userPrincipal);
    }

    @Override
    public void deleteComment(Long articleCommentId) {
        // TODO: 댓글이 존재할 경우에 삭제할 수 있다
        articleCommentRepository.deleteById(articleCommentId);
    }
}
