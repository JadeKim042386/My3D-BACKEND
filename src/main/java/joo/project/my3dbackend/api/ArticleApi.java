package joo.project.my3dbackend.api;

import com.querydsl.core.types.Predicate;
import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.SubscribeStatus;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.response.ApiResponse;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.ArticleFileServiceInterface;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import joo.project.my3dbackend.service.FileServiceInterface;
import joo.project.my3dbackend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleApi {
    private final ArticleServiceInterface articleService;
    private final ArticleFileServiceInterface articleFileService;
    private final FileServiceInterface fileService;

    /**
     * 게시글 목록 조회 (페이지당 9개)
     * - 제목, 카테고리 검색: /api/v1/articles?title=title&articleCategory=MUSIC
     * - 작성일시 기준 정렬: /api/v1/articles?sort=createdAt,asc
     * - 좋아요 기준 정렬: /api/v1/articles?sort=likeCount,asc
     */
    @GetMapping
    public ResponseEntity<Page<ArticleDto>> getArticles(
            @QuerydslPredicate(root = Article.class) Predicate predicate,
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(articleService.getArticles(predicate, pageable));
    }

    /**
     * 게시글 단일 조회 요청
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDto> getArticle(
            @PathVariable Long articleId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        checkSubscribeStatusIfPaidArticle(userPrincipal, articleId);
        ArticleDto article = articleService.getArticleDtoWithFile(articleId);
        return ResponseEntity.ok(article);
    }

    /**
     * 게시글 작성 요청
     * <pre>
     * - optional: ArticleCategory, DimensionOptionRequest, modelFile
     * </pre>
     */
    @PostMapping
    public ResponseEntity<ArticleDto> writeArticle(
            @RequestPart(required = false) MultipartFile modelFile,
            @RequestPart @Valid ArticleRequest articleRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ArticleDto articleDto = articleService.writeArticle(modelFile, articleRequest, userPrincipal);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleDto);
    }

    /**
     * 특정 게시글 수정 요청
     */
    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleDto> postUpdateArticle(
            @PathVariable Long articleId,
            @RequestPart(required = false) MultipartFile modelFile,
            @RequestPart @Valid ArticleRequest articleRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        checkIfWriterOrAdmin(userPrincipal, articleId);
        ArticleDto articleDto = articleService.updateArticle(modelFile, articleRequest, articleId, userPrincipal);
        return ResponseEntity.ok(articleDto);
    }

    /**
     * 게시글 삭제 요청
     */
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<String>> deleteArticle(
            @PathVariable Long articleId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        checkIfWriterOrAdmin(userPrincipal, articleId);
        // TODO: 삭제시 comment, like, file select 발생
        articleService.deleteArticle(articleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.of("you're successfully delete article"));
    }

    /**
     * 특정 게시글의 모델 파일 다운로드 요청
     */
    @GetMapping("/{articleId}/download")
    public ResponseEntity<byte[]> downloadArticleFile(@PathVariable Long articleId) {
        String fileName = articleFileService.findUploadedFileName(articleId);
        byte[] file = fileService.downloadFile(fileName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(file.length);
        httpHeaders.setContentDispositionFormData("attachment", "model." + FileUtils.getExtension(fileName));
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(file);
    }

    /**
     * 게시글 작성자 또는 관리자가 맞는지 확인
     */
    private void checkIfWriterOrAdmin(UserPrincipal userPrincipal, Long articleId) {
        if (userPrincipal.getUserRole() != UserRole.ADMIN
                && articleService.isWriterOfArticle(userPrincipal.id(), articleId))
            throw new ArticleException(ErrorCode.FORBIDDEN_ARTICLE);
    }

    /**
     * 유료 게시글일 경우 구독 상태인지 확인
     */
    private void checkSubscribeStatusIfPaidArticle(UserPrincipal userPrincipal, Long articleId) {
        if (!articleService.isFreeArticle(articleId) && userPrincipal.subscribeStatus() != SubscribeStatus.SUBSCRIBE)
            throw new ArticleException(ErrorCode.FORBIDDEN_ARTICLE);
    }
}
