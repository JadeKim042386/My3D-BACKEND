package joo.project.my3dbackend.service;

import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.ArticleWithCommentDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;

public interface ArticleServiceInterface {

    /**
     * 게시글 단일 조회 (댓글 포함)
     */
    ArticleWithCommentDto getArticleWithComment(Long articleId);

    /**
     * 게시글 작성
     */
    ArticleDto writeArticle(ArticleRequest articleRequest, UserPrincipal userPrincipal);

    /**
     * 게시글 삭제
     */
    void deleteArticle(Long articleId);
}
