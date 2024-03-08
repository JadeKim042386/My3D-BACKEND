package joo.project.my3dbackend.service;

import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ArticleServiceInterface {
    /**
     * 게시글 목록 조회
     */
    Page<ArticleDto> getArticles(Pageable pageable);

    /**
     * 게시글 단일 조회
     */
    ArticleDto getArticle(Long articleId);

    /**
     * 게시글 작성
     */
    ArticleDto writeArticle(Optional<MultipartFile> modelFile, ArticleRequest articleRequest, UserPrincipal userPrincipal);

    /**
     * 게시글 삭제
     */
    void deleteArticle(Long articleId);
}
