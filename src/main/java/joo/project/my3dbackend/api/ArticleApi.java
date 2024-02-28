package joo.project.my3dbackend.api;

import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleApi {
    private final ArticleServiceInterface articleService;

    /**
     * 게시글 작성 요청
     * <pre>
     * - 모델 파일, 모델 파일에 대한 치수는 optional
     * </pre>
     */
    @PostMapping
    public ResponseEntity<?> writeArticle(
            @RequestBody @Valid ArticleRequest articleRequest, BindingResult bindingResult, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (bindingResult.hasErrors()) {
            log.error("bindingResult: {}", bindingResult);
            // TODO: bindingResult에서 어떤 필드가 문제인지 메시지와 함께 전달
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ArticleException(ErrorCode.FAILED_WRITE_ARTICLE));
        }
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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("you're successfully delete article");
    }
}
