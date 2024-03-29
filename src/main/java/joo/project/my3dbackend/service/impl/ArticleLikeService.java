package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.api.constants.LikeStatus;
import joo.project.my3dbackend.domain.ArticleLike;
import joo.project.my3dbackend.repository.ArticleLikeRepository;
import joo.project.my3dbackend.repository.ArticleRepository;
import joo.project.my3dbackend.service.ArticleLikeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService implements ArticleLikeServiceInterface {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;

    @Override
    public int updateLikeCount(LikeStatus likeStatus, Long articleId, Long userAccountId) {
        if (likeStatus == LikeStatus.LIKE) {
            return addArticleLike(articleId, userAccountId);
        } else {
            return deleteArticleLike(articleId, userAccountId);
        }
    }

    @Override
    public int addArticleLike(Long articleId, Long userAccountId) {
        articleLikeRepository.save(ArticleLike.of(userAccountId, articleId));
        return articleRepository.addArticleLikeCount(articleId);
    }

    @Override
    public int deleteArticleLike(Long articleId, Long userAccountId) {
        articleLikeRepository.deleteByArticleIdAndUserAccountId(articleId, userAccountId);
        return articleRepository.deleteArticleLikeCount(articleId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAddedLike(Long articleId, Long userAccountId) {
        return articleLikeRepository.existsByArticleIdAndUserAccountId(articleId, userAccountId);
    }
}
