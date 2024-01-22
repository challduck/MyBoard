package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.ArticleLike;
import dev.challduck.portfolio.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    boolean existsByMemberIdAndArticleId(Member member, Article article);

    @Query(value = "select count(al) from ArticleLike as al where al.articleId = :article")
    Long countByArticleId(@Param("article") Article article);

}
