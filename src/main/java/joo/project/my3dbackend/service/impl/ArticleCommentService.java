package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.repository.ArticleCommentRepository;
import joo.project.my3dbackend.service.AlarmServiceInterface;
import joo.project.my3dbackend.service.ArticleCommentServiceInterface;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService implements ArticleCommentServiceInterface {
    private final ArticleCommentRepository articleCommentRepository;
    private final AlarmServiceInterface<SseEmitter> alarmService;
    private final ArticleServiceInterface articleService;

    @Transactional(readOnly = true)
    @Override
    public Page<ArticleCommentDto> getComments(Pageable pageable, Long parentCommentId) {
        return articleCommentRepository.findAll(pageable, parentCommentId).map(ArticleCommentDto::fromEntity);
    }

    @Override
    public ArticleCommentDto writeComment(
            ArticleCommentRequest articleCommentRequest, UserPrincipal userPrincipal, Long articleId) {

        ArticleComment savedArticleComment =
                articleCommentRepository.save(articleCommentRequest.toEntity(userPrincipal.id(), articleId));
        // 작성자가 아닌 경우만 알람을 전송한다
        if (!articleService.isWriterOfArticle(userPrincipal.id(), articleId)) {
            Long receiverId = articleService.getUserAccountIdOfArticle(articleId);
            alarmService.send(articleId, savedArticleComment.getId(), userPrincipal.id(), receiverId);
        }
        return ArticleCommentDto.fromEntity(savedArticleComment, userPrincipal);
    }

    @Override
    public void deleteComment(Long articleCommentId) {
        // TODO: 댓글이 존재할 경우에 삭제할 수 있다
        articleCommentRepository.deleteByIdOrParentCommentId(articleCommentId);
    }
}
