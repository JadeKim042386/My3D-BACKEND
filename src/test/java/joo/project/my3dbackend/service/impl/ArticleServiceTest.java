package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.dto.ArticleWithCommentDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArticleServiceTest {
    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Order(0)
    @DisplayName("게시글 작성")
    @Test
    void writeArticle() {
        // given
        Long userAccountId = 1L;
        ArticleRequest articleRequest = FixtureDto.createArticleRequest();
        UserPrincipal userPrincipal = FixtureDto.createUserPrincipal();
        given(articleRepository.save(any(Article.class))).willReturn(articleRequest.toEntity(userAccountId));
        // when
        assertThatNoException().isThrownBy(() -> articleService.writeArticle(articleRequest, userPrincipal));
        // then
    }

    @Order(1)
    @DisplayName("게시글 삭제")
    @Test
    void deleteArticle() {
        // given
        Long articleId = 1L;
        // when
        assertThatNoException().isThrownBy(() -> articleService.deleteArticle(articleId));
        // then
    }

    @Order(2)
    @DisplayName("게시글 단일 조회")
    @Test
    void getArticle() {
        // given
        Long articleId = 1L;
        given(articleRepository.findFetchAllById(anyLong())).willReturn(Optional.of(Fixture.createArticleWithComment()));
        // when
        ArticleWithCommentDto articleWithComment = articleService.getArticleWithComment(articleId);
        // then
        assertThat(articleWithComment.article().title()).isEqualTo("title");
        assertThat(articleWithComment.articleComments().size()).isEqualTo(1);
    }
}