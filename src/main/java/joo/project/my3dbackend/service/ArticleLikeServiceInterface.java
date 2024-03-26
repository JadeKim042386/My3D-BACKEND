package joo.project.my3dbackend.service;

import joo.project.my3dbackend.api.constants.LikeStatus;

public interface ArticleLikeServiceInterface {

    /**
     * likeStatus(LIKE, UNLIKE) 값에 따라 좋아요를 추가/삭제
     */
    int updateLikeCount(LikeStatus likeStatus, Long articleId, Long userAccountId);

    /**
     * 좋아요 추가
     */
    int addArticleLike(Long articleId, Long userAccountId);

    /**
     * 좋아요 삭제
     */
    int deleteArticleLike(Long articleId, Long userAccountId);

    /**
     * 좋아요 추가 여부 조회
     */
    boolean isAddedLike(Long articleId, Long userAccountId);
}
