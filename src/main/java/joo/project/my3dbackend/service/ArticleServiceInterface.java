package joo.project.my3dbackend.service;

import com.querydsl.core.types.Predicate;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleServiceInterface {
    /**
     * 게시글 목록 조회
     */
    Page<ArticleDto> getArticles(Predicate predicate, Pageable pageable);

    /**
     * 게시글 단일 조회
     */
    ArticleDto getArticle(Long articleId);

    /**
     * 게시글 작성
     */
    ArticleDto writeArticle(MultipartFile modelFile, ArticleRequest articleRequest, UserPrincipal userPrincipal);

    /**
     * 게시글 삭제
     */
    void deleteArticle(Long articleId);

    /**
     * 게시글 작성자 ID 조회
     */
    Long getUserAccountIdOfArticle(Long articleId);

    /**
     * 게시글 작성자인지 확인
     */
    boolean isWriterOfArticle(Long userAccountId, Long articleId);

    /**
     * 무료 게시글인지 확인
     */
    boolean isFreeArticle(Long articleId);
}
