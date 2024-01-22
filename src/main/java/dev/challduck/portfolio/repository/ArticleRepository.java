package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "select a from Article as a left join fetch a.member order by a.articleId desc")
    List<Article> findAllByOrderByArticleIdDesc();

    @Query(value = "select a from Article as a left join fetch a.comments left join fetch a.member where a.articleId = :articleId")
    Article findByArticleId(@Param("articleId") Long id);
}
