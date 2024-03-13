package joo.project.my3dbackend.service;

public interface ArticleLikeServiceInterface {

    /**
     * 좋아요 추가
     */
    int addArticleLike(Long articleId, Long userAccountId);

    /**
     * 좋아요 삭제
     */
    int deleteArticleLike(Long articleId, Long userAccountId);
}
