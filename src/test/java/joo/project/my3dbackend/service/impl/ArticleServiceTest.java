package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        ArticleRequest articleRequest = FixtureDto.createArticleRequest();
        given(articleRepository.save(any(Article.class))).willReturn(articleRequest.toEntity());
        // when
        ArticleDto articleDto = articleService.writeArticle(articleRequest);
        // then
        assertThat(articleDto.title()).isEqualTo("title");
        assertThat(articleDto.content()).isEqualTo("content");
        assertThat(articleDto.articleCategory()).isEqualTo(ArticleCategory.MUSIC);
        assertThat(articleDto.isFree()).isEqualTo(true);
    }

    @Order(1)
    @DisplayName("게시글 삭제")
    @Test
    void deleteArticle() {
        // given
        Long articleId = 1L;
        // when
        Assertions.assertThatNoException().isThrownBy(() -> articleService.deleteArticle(articleId));
        // then
    }
}