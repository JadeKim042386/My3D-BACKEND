package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    //TODO: childComments의 크기가 커질 경우를 생각하여 페이징 고려 (with 부모 댓글)
    @EntityGraph(attributePaths = "childComments")
    Optional<ArticleComment> findById(Long articleCommentId);
}
