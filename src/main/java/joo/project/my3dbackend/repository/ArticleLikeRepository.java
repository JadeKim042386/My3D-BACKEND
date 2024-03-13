package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    void deleteByArticleIdAndUserAccountId(Long articleId, Long userAccountId);
}
