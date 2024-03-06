package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    Optional<ArticleComment> findById(Long articleCommentId);

    /**
     * parentCommentId를 가지는 대댓글 목록 조회
     */
    @Query(
            """
            select ac
            from ArticleComment ac
            where ac.parentCommentId = ?1
            """)
    @EntityGraph(attributePaths = "userAccount", type = LOAD)
    Page<ArticleComment> findAllChildComments(Pageable pageable, Long parentCommentId);
}
