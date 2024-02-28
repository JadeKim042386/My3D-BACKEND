package joo.project.my3dbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import joo.project.my3dbackend.config.TestSecurityConfig;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleApi.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArticleApiTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleServiceInterface articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Order(0)
    @DisplayName("게시글 작성")
    @Test
    void writeArticle() throws Exception {
        // given
        ArticleRequest articleRequest = FixtureDto.createArticleRequest();
        Long userAccountId = 1L;
        UserPrincipal userPrincipal = FixtureDto.createUserPrincipal();
        given(articleService.writeArticle(any(ArticleRequest.class), any(UserPrincipal.class)))
                .willReturn(ArticleDto.fromEntity(articleRequest.toEntity(userAccountId), userPrincipal));
        // when
        mvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.articleCategory").value("MUSIC"))
                .andExpect(jsonPath("$.isFree").value("true"));
        // then
    }

    @Order(1)
    @DisplayName("게시글 작성 Validation Error")
    @ParameterizedTest
    @CsvSource(
            value = {
                "title, content, MUSIC, ",
                "title, content, , true",
                "title, , MUSIC, true",
                ", content, music, true",
                "title, content, NULL, true",
            })
    void writeArticle_invalid(String title, String content, String articleCategory, Boolean isFree) throws Exception {
        // given
        ArticleRequest articleRequest = FixtureDto.createArticleRequest(title, content, articleCategory, isFree);
        // when
        mvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleRequest)))
                .andExpect(status().isBadRequest());
        // then
    }

    @Order(2)
    @DisplayName("게시글 삭제")
    @Test
    void deleteArticle() throws Exception {
        // given
        Long articleId = 1L;
        // when
        mvc.perform(delete("/api/v1/articles/" + articleId)).andExpect(status().isNoContent());
        // then
    }

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Order(3)
    @DisplayName("게시글 단일 조회")
    @Test
    void getArticle() throws Exception {
        //given
        Long articleId = 1L;
        given(articleService.getArticleWithComment(anyLong()))
                .willReturn(FixtureDto.createArticleWithCommentDto());
        //when
        mvc.perform(get("/api/v1/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title").value("title"))
                .andExpect(jsonPath("$.articleComments[0].content").value("content"));
        //then
    }
}