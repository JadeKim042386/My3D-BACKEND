package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @EntityGraph(
            attributePaths = {"userAccount", "articleFile"},
            type = LOAD)
    Page<Article> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"userAccount", "articleFile"})
    Optional<Article> findFetchAllById(Long articleId);

    @Query("select a.userAccountId from Article a where a.id=?1")
    Optional<Long> findUserAccountIdById(Long articleId);

    boolean existsByIdAndUserAccount_Id(Long articleId, Long userAccountId);

    /**
     * 좋아요 수 + 1
     */
    @Query(
            nativeQuery = true,
            value = "update article set like_count = like_count + 1 where id = ? returning like_count")
    int addArticleLikeCount(Long articleId);

    /**
     * 좋아요 수 - 1
     */
    @Query(
            nativeQuery = true,
            value = "update article set like_count = like_count - 1 where id = ? returning like_count")
    int deleteArticleLikeCount(Long articleId);
}
