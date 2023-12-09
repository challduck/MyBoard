package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentViewResponse {
    private final String body;
    private final String nickname;
    private final LocalDateTime createdAt;

    public CommentViewResponse(Comment comment, Member member){
        this.body = comment.getBody();
        this.nickname = member.getNickname();
        this.createdAt = comment.getCreatedAt();
    }
}
