package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.ArticleWithCommentDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleApi {
    private final ArticleServiceInterface articleService;

    /**
     * 게시글 목록 조회 (페이지당 9개)
     */
    @GetMapping
    public ResponseEntity<Page<ArticleDto>> getArticles(
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticles(pageable));
    }

    /**
     * 게시글 단일 조회 요청 (댓글 포함)
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleWithCommentDto> getArticle(@PathVariable Long articleId) {
        ArticleWithCommentDto articleWithComment = articleService.getArticleWithComment(articleId);
        return ResponseEntity.ok(articleWithComment);
    }

    /**
     * 게시글 작성 요청
     * <pre>
     * - 모델 파일, 모델 파일에 대한 치수는 optional
     * </pre>
     */
    @PostMapping
    public ResponseEntity<ArticleDto> writeArticle(
            @RequestBody @Valid ArticleRequest articleRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ArticleDto articleDto = articleService.writeArticle(articleRequest, userPrincipal);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleDto);
    }

    /**
     * 게시글 삭제 요청
     */
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
        // TODO: 작성자 또는 관리자만 게시글을 삭제할 수 있다.
        articleService.deleteArticle(articleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.of("you're successfully delete article"));
    }
}
