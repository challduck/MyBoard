package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByArticleIdDesc();

//    List<Article> findByArticleId(Long id);
}
