package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {}
