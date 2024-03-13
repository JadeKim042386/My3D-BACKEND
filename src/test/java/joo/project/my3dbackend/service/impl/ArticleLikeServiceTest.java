package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.ArticleLike;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.repository.ArticleLikeRepository;
import joo.project.my3dbackend.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ArticleLikeServiceTest {
    @InjectMocks private ArticleLikeService articleLikeService;
    @Mock private ArticleLikeRepository articleLikeRepository;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("좋아요 추가")
    @Test
    void addArticleLike() {
        //given
        Long articleId = 1L, userAccountId = 1L;
        given(articleLikeRepository.save(any(ArticleLike.class))).willReturn(Fixture.createArticleLike());
        given(articleRepository.addArticleLikeCount(anyLong())).willReturn(2);
        //when
        int likeCount = articleLikeService.addArticleLike(articleId, userAccountId);
        //then
        assertThat(likeCount).isEqualTo(2);
    }

    @DisplayName("좋아요 삭제")
    @Test
    void deleteArticleLike() {
        //given
        Long articleId = 1L, userAccountId = 1L;
        willDoNothing().given(articleLikeRepository).deleteByArticleIdAndUserAccountId(anyLong(), anyLong());;
        given(articleRepository.deleteArticleLikeCount(anyLong())).willReturn(1);
        //when
        int likeCount = articleLikeService.deleteArticleLike(articleId, userAccountId);
        //then
        assertThat(likeCount).isEqualTo(1);
    }
}