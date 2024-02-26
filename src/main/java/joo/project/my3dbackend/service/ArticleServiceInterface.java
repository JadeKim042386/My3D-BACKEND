package joo.project.my3dbackend.service;

import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;

public interface ArticleServiceInterface {

    /**
     * 게시글 작성
     */
    ArticleDto writeArticle(ArticleRequest articleRequest);
}
