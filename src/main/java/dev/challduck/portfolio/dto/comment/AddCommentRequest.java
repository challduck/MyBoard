package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.domain.Member;
import lombok.Getter;

@Getter
public class AddCommentRequest {
    private Member member;
    private Article article;
    private String body;

    public Comment toEntity(Member member, Article article){
        return Comment.builder()
                .article(article)
                .member(member)
                .body(body)
                .build();
    }
}
