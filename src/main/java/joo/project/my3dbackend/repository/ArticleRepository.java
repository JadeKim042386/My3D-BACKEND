package joo.project.my3dbackend.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EnumExpression;
import com.querydsl.core.types.dsl.StringExpression;
import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.QArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

public interface ArticleRepository
        extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, QuerydslBinderCustomizer<QArticle> {

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.articleCategory);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.articleCategory).first(EnumExpression::eq);
    }

    @EntityGraph(
            attributePaths = {"userAccount", "articleFile"},
            type = LOAD)
    Page<Article> findAll(Predicate predicate, Pageable pageable);

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

    @Query("select a.isFree from Article a where a.id=?1")
    boolean getArticleIsFreeStatus(Long articleId);
}
