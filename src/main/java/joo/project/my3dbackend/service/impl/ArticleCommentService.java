package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.repository.ArticleCommentRepository;
import joo.project.my3dbackend.service.ArticleCommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleCommentService implements ArticleCommentServiceInterface {
    private final ArticleCommentRepository articleCommentRepository;

    @Override
    public ArticleCommentDto writeComment(ArticleCommentRequest articleCommentRequest, Long userAccountId, Long articleId) {
        ArticleComment savedArticleComment = articleCommentRepository.save(
                articleCommentRequest.toEntity(userAccountId, articleId)
        );
        return ArticleCommentDto.fromEntity(savedArticleComment);
    }

    @Override
    public void deleteComment(Long articleCommentId) {
        articleCommentRepository.deleteById(articleCommentId);
    }
}
