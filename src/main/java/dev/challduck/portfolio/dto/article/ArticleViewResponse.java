package dev.challduck.portfolio.dto.article;

import dev.challduck.portfolio.domain.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleViewResponse {
    private final Long id;
    private final String title;
    private final String content;
    @Schema(description = "게시글 작성자", example = "admin")
    private final String nickname;
    @Schema(description = "게시글 조회수", example = "0")
    private final Long hitCount;
    private final LocalDateTime created_at;
    public ArticleViewResponse(Article article){
        this.id = article.getArticleId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.created_at = article.getCreatedAt();
        this.hitCount = article.getHitCount();
        this.nickname = article.getMember().getNickname();
    }
}
