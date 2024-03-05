package joo.project.my3dbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import joo.project.my3dbackend.config.TestSecurityConfig;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.fixture.FixtureDto;
import joo.project.my3dbackend.service.impl.ArticleCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleCommentApi.class)
class ArticleCommentApiTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleCommentService articleCommentService;

    @WithUserDetails(value = "testUser@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("댓글 추가")
    @Test
    void writeComment() throws Exception {
        // given
        Long userAccountId = 1L;
        Long articleId = 1L;
        ArticleCommentRequest articleCommentRequest = FixtureDto.createArticleCommentRequest();
        ArticleCommentDto articleCommentDto = FixtureDto.createArticleCommentDto();
        given(articleCommentService.writeComment(any(ArticleCommentRequest.class), anyLong(), anyLong()))
                .willReturn(articleCommentDto);
        // when
        mvc.perform(post("/api/v1/articles/" + articleId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCommentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("content"));
        // then
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() throws Exception {
        // given
        Long articleId = 1L;
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentService).deleteComment(anyLong());
        // when
        mvc.perform(delete("/api/v1/articles/" + articleId + "/comments/" + articleCommentId))
                .andExpect(status().isNoContent());
        // then
    }
}
