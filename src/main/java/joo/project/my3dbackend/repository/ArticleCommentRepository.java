package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    @EntityGraph(attributePaths = "childComments")
    Optional<ArticleComment> findById(Long articleCommentId);
}
