package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @EntityGraph(attributePaths = "userAccount", type = LOAD)
    Page<Article> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"userAccount", "articleComments"})
    Optional<Article> findFetchAllById(Long articleId);
}
