package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.ArticleLike;
import dev.challduck.portfolio.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    boolean existsByMemberIdAndArticleId(Member member, Article article);
    Long countByArticleId(Article article);
    ArticleLike findByArticleId(Article article);
}
