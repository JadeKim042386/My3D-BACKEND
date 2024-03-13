package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.response.ArticleLikeResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.ArticleLikeServiceInterface;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles/{articleId}/like")
@RequiredArgsConstructor
public class ArticleLikeApi {
    private final ArticleLikeServiceInterface articleLikeService;
    private final ArticleServiceInterface articleService;

    /**
     * 특정 게시글에 좋아요 추가
     */
    @PostMapping
    public ResponseEntity<ArticleLikeResponse> addArticleLike(
            @PathVariable Long articleId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        isWriter(articleId, userPrincipal.id());
        // TODO: 좋아요를 추가하지 않았을 경우에만 추가할 수 있음
        return ResponseEntity.ok(
                ArticleLikeResponse.of(articleLikeService.addArticleLike(articleId, userPrincipal.id())));
    }

    /**
     * 특정 게시글의 좋아요 해제
     */
    @DeleteMapping
    public ResponseEntity<ArticleLikeResponse> deleteArticleLike(
            @PathVariable Long articleId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        isWriter(articleId, userPrincipal.id());
        // TODO: 좋아요를 추가했을 경우만 삭제할 수 있음
        return ResponseEntity.ok(
                ArticleLikeResponse.of(articleLikeService.deleteArticleLike(articleId, userPrincipal.id())));
    }

    /**
     * 작성자는 좋아요를 추가하거나 취소할 수 없음
     */
    private void isWriter(Long articleId, Long userAccountId) {
        if (articleService.isWriterOfArticle(articleId, userAccountId)) {
            throw new ArticleException(ErrorCode.INVALID_REQUEST);
        }
    }
}
