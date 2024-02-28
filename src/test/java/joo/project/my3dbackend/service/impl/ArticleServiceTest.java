package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.dto.ArticleDto;
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

import static org.assertj.core.api.Assertions.assertThat;
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
        given(articleRepository.save(any(Article.class)))
                .willReturn(articleRequest.toEntity(userAccountId));
        // when
        ArticleDto articleDto = articleService.writeArticle(articleRequest, userPrincipal);
        // then
        assertThat(articleDto.title()).isEqualTo("title");
        assertThat(articleDto.content()).isEqualTo("content");
        assertThat(articleDto.articleCategory()).isEqualTo(ArticleCategory.MUSIC);
        assertThat(articleDto.isFree()).isEqualTo(true);
    }
}