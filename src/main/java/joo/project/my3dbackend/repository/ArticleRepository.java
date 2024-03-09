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
    @EntityGraph(attributePaths = "userAccount", type = LOAD)
    Page<Article> findAll(Pageable pageable);

    /**
     * 부모 댓글을 가지는 게시글 조회 (대댓글은 조회하지 않음)
     */
    @Query("""
            select distinct a
            from Article a
            left outer join fetch a.userAccount ua
            left outer join fetch a.articleComments ac
            where a.id = ?1 and ac.parentCommentId is null
            """)
    Optional<Article> findFetchAllById(Long articleId);
}
