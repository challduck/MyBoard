package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
}
