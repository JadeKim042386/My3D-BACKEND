package joo.project.my3dbackend.api;

import joo.project.my3dbackend.config.TestSecurityConfig;
import joo.project.my3dbackend.service.impl.ArticleLikeService;
import joo.project.my3dbackend.service.impl.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleLikeApi.class)
class ArticleLikeApiTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleLikeService articleLikeService;

    @MockBean
    private ArticleService articleService;

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("좋아요 추가")
    @Test
    void addArticleLike() throws Exception {
        // given
        Long articleId = 1L;
        int updatedLikeCount = 2;
        given(articleService.doesArticleExistByUserAccountId(anyLong(), anyLong()))
                .willReturn(false);
        given(articleLikeService.addArticleLike(anyLong(), anyLong())).willReturn(updatedLikeCount);
        // when
        mvc.perform(post("/api/v1/articles/" + articleId + "/like").param("likeStatus", "LIKE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(updatedLikeCount));
        // then
    }

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("좋아요 삭제")
    @Test
    void deleteArticleLike() throws Exception {
        // given
        Long articleId = 1L;
        int updatedLikeCount = 1;
        given(articleService.doesArticleExistByUserAccountId(anyLong(), anyLong()))
                .willReturn(false);
        given(articleLikeService.deleteArticleLike(anyLong(), anyLong())).willReturn(updatedLikeCount);
        // when
        mvc.perform(post("/api/v1/articles/" + articleId + "/like").param("likeStatus", "UNLIKE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(updatedLikeCount));
        // then
    }
}