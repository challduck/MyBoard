package dev.challduck.portfolio.repository;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Article articleId);
}
