package joo.project.my3dbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import joo.project.my3dbackend.config.TestSecurityConfig;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.fixture.Fixture;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import joo.project.my3dbackend.service.impl.ArticleFileService;
import joo.project.my3dbackend.service.impl.LocalFileService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

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
    @MockBean
    private ArticleFileService articleFileService;
    @MockBean
    private LocalFileService fileService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Order(0)
    @DisplayName("모델 게시글 작성")
    @Test
    void writeArticle() throws Exception {
        // given
        ArticleRequest articleRequest = FixtureDto.createArticleRequest();
        Long userAccountId = 1L;
        UserPrincipal userPrincipal = FixtureDto.createUserPrincipal();
        MockMultipartFile modelFile = Fixture.createMultipartFile();
        given(articleService.writeArticle(
                        any(MultipartFile.class), any(ArticleRequest.class), any(UserPrincipal.class)))
                .willReturn(ArticleDto.fromEntity(articleRequest.toEntity(userAccountId, modelFile), userPrincipal));
        // when
        mvc.perform(multipart("/api/v1/articles")
                        .file(modelFile)
                        .file(new MockMultipartFile(
                                "articleRequest",
                                "",
                                "application/json",
                                objectMapper.writeValueAsBytes(articleRequest)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.articleCategory").value("MUSIC"))
                .andExpect(jsonPath("$.isFree").value("true"));
        // then
    }

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Order(1)
    @DisplayName("게시글 작성 Validation Error (title, content, isFree)")
    @ParameterizedTest
    @CsvSource(
            value = {
                "title, content, MODEL, MUSIC, ",
                "title, , MODEL, MUSIC, true",
                ", content, MODEL, MUSIC, true"
            })
    void writeArticle_invalid(String title, String content, ArticleType articleType, ArticleCategory articleCategory, Boolean isFree)
            throws Exception {
        // given
        ArticleRequest articleRequest = FixtureDto.createArticleRequest(title, content, articleType, articleCategory, isFree);
        MockMultipartFile modelFile = Fixture.createMultipartFile();
        // when
        mvc.perform(multipart("/api/v1/articles")
                        .file(modelFile)
                        .file(new MockMultipartFile(
                                "articleRequest",
                                "",
                                "application/json",
                                objectMapper.writeValueAsBytes(articleRequest)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        // then
    }

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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
        // given
        Long articleId = 1L;
        given(articleService.isFreeArticle(anyLong())).willReturn(true);
        given(articleService.getArticle(anyLong())).willReturn(FixtureDto.createArticleDto());
        // when
        mvc.perform(get("/api/v1/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
        // then
    }
}