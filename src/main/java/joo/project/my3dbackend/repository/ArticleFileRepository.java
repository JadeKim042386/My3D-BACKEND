package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Long> {
    @Query("select a.articleFile.fileName from Article a where a.id = ?1")
    Optional<String> findFileNameByArticleId(Long articleId);
}
