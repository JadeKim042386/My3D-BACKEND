package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    Optional<ArticleComment> findById(Long articleCommentId);

    /**
     * 댓글 목록 조회
     */
    @Query(
            """
            select ac
            from ArticleComment ac
            where (?1 is not null and ac.parentCommentId = ?1) or (?1 is null and ac.parentCommentId is null)
            """)
    @EntityGraph(attributePaths = "userAccount", type = LOAD)
    Page<ArticleComment> findAll(Pageable pageable, Long parentCommentId);

    /**
     * 댓글 삭제
     * - 부모 댓글을 삭제할 경우 자식 댓글의 parentId를 확인하여 같이 삭제
     */
    @Modifying
    @Query("delete from ArticleComment ac where ac.id=?1 or ac.parentCommentId=?1")
    void deleteByIdOrParentCommentId(Long articleCommentId);

    boolean existsByIdAndUserAccount_Id(Long articleCommentId, Long userAccountId);
}
