package dev.challduck.portfolio.dto.article;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;
    private String content;
    private String author;

    public Article toEntity(Member member){
        return Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}
