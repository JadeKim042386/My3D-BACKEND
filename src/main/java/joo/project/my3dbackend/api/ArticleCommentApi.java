package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.service.ArticleCommentServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles/{articleId}/comments")
@RequiredArgsConstructor
public class ArticleCommentApi {
    private final ArticleCommentServiceInterface articleCommentService;
    // TODO: 대상 게시글이 존재하는지 확인

    /**
     * 댓글 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<ArticleCommentDto>> getComments(
            @PathVariable Long articleId,
            @RequestParam(required = false) Long parentCommentId,
            @PageableDefault Pageable pageable) {
        // TODO: parentCommentId이 지정될 경우 부모 댓글이 존재하는지 확인
        Page<ArticleCommentDto> comments = articleCommentService.getComments(pageable, parentCommentId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 추가
     */
    @PostMapping
    public ResponseEntity<ArticleCommentDto> writeComment(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleCommentRequest articleCommentRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ArticleCommentDto articleCommentDto =
                articleCommentService.writeComment(articleCommentRequest, userPrincipal, articleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleCommentDto);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{articleCommentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long articleId, @PathVariable Long articleCommentId) {
        // TODO: 댓글 작성자 또는 관리자가 맞는지 확인
        articleCommentService.deleteComment(articleCommentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.of("you're successfully delete comment"));
    }
}
