package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.repository.ArticleCommentRepository;
import joo.project.my3dbackend.service.AlarmServiceInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService articleCommentService;

    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Mock
    private AlarmServiceInterface<SseEmitter> alarmService;

    @DisplayName("댓글 추가")
    @Test
    void writeComment() {
        // given
        Long userAccountId = 1L;
        Long articleId = 1L;
        ArticleCommentRequest articleCommentRequest = FixtureDto.createArticleCommentRequest();
        UserPrincipal userPrincipal = FixtureDto.createUserPrincipal();
        ArticleComment articleComment = articleCommentRequest.toEntity(userAccountId, articleId);
        ReflectionTestUtils.setField(articleComment, "id", 1L);
        given(articleCommentRepository.save(any(ArticleComment.class)))
                .willReturn(articleComment);
        willDoNothing().given(alarmService).send(anyLong(), anyLong(), anyLong(), anyLong());
        // when
        ArticleCommentDto articleCommentDto =
                articleCommentService.writeComment(articleCommentRequest, userPrincipal, articleId);
        // then
        assertThat(articleCommentDto.content()).isEqualTo("content");
    }

    @DisplayName("대댓글 추가")
    @Test
    void writeChildComment() {
        // given
        Long articleId = 1L;
        ArticleCommentRequest articleCommentRequest = FixtureDto.createArticleCommentRequest("content", 1L);
        UserPrincipal userPrincipal = FixtureDto.createUserPrincipal();
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(Fixture.createArticleComment(1L));
        willDoNothing().given(alarmService).send(anyLong(), anyLong(), anyLong(), anyLong());
        // when
        ArticleCommentDto articleCommentDto =
                articleCommentService.writeComment(articleCommentRequest, userPrincipal, articleId);
        // then
        assertThat(articleCommentDto.parentCommentId()).isEqualTo(articleId);
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() {
        // given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteByIdOrParentCommentId(anyLong());
        // when
        assertThatNoException().isThrownBy(() -> articleCommentService.deleteComment(articleCommentId));
        // then
    }
}
