package joo.project.my3dbackend.api;

import joo.project.my3dbackend.api.constants.LikeStatus;
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
     * 특정 게시글에 좋아요 추가/삭제
     */
    @PostMapping
    public ResponseEntity<ArticleLikeResponse> addOrDeleteArticleLike(
            @RequestParam LikeStatus likeStatus,
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        checkIfWriter(articleId, userPrincipal.id());
        // TODO: 좋아요를 추가하지 않았을 경우에만 추가할 수 있고 좋아요를 추가했을 경우만 삭제할 수 있음
        return ResponseEntity.ok(
                new ArticleLikeResponse(articleLikeService.updateLikeCount(likeStatus, articleId, userPrincipal.id())));
    }

    /**
     * 작성자는 좋아요를 추가하거나 취소할 수 없음
     */
    private void checkIfWriter(Long articleId, Long userAccountId) {
        if (articleService.isWriterOfArticle(articleId, userAccountId)) {
            throw new ArticleException(ErrorCode.INVALID_REQUEST);
        }
    }
}
