package dev.challduck.portfolio.dto.article;

import dev.challduck.portfolio.domain.Article;
import lombok.Getter;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    private final String nickname;
    public ArticleResponse(Article article, String nickname){
        this.title = article.getTitle();
        this.content = article.getContent();
        this.nickname = nickname;
    }
}
