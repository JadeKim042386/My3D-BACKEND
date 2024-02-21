package joo.project.my3dbackend.repository;

import joo.project.my3dbackend.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {}
