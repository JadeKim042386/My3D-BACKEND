package joo.project.my3dbackend.api;

import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.CommentException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.ArticleCommentServiceInterface;
import joo.project.my3dbackend.service.ArticleServiceInterface;
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
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles/{articleId}/comments")
@RequiredArgsConstructor
public class ArticleCommentApi {
    private final ArticleCommentServiceInterface articleCommentService;
    private final ArticleServiceInterface articleService;

    /**
     * 댓글 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<ArticleCommentDto>> getComments(
            @PathVariable Long articleId,
            @RequestParam(required = false) Long parentCommentId,
            @PageableDefault Pageable pageable) {
        checkIfExistsArticle(articleId);
        if (!Objects.isNull(parentCommentId)) checkIfExistsParentComment(parentCommentId);
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
    public ResponseEntity<?> deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long articleCommentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        checkIfExistsArticle(articleId);
        checkIfWriterOrAdmin(userPrincipal, articleCommentId);
        articleCommentService.deleteComment(articleCommentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.of("you're successfully delete comment"));
    }

    /**
     * 댓글 작성자 또는 관리자가 맞는지 확인
     */
    private void checkIfWriterOrAdmin(UserPrincipal userPrincipal, Long articleCommentId) {
        if (userPrincipal.getUserRole() != UserRole.COMPANY
                && articleCommentService.isWriterOfComment(userPrincipal.id(), articleCommentId))
            throw new CommentException(ErrorCode.FORBIDDEN_COMMENT);
    }

    /**
     * parentCommentId이 지정될 경우 부모 댓글이 존재하는지 확인
     */
    private void checkIfExistsParentComment(Long articleCommentId) {
        if (!articleCommentService.existsComment(articleCommentId))
            throw new CommentException(ErrorCode.NOT_FOUND_COMMENT);
    }

    /**
     * 게시글이 존재하는지 확인
     */
    private void checkIfExistsArticle(Long articleId) {
        if (!articleService.existsArticle(articleId)) throw new ArticleException(ErrorCode.NOT_FOUND_ARTICLE);
    }
}
