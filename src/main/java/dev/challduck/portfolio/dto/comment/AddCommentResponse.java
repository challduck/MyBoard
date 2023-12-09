package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AddCommentResponse {
    private String body;
    private String nickname;
    private LocalDateTime created_at;

    public AddCommentResponse(Comment comment){
        this.body = comment.getBody();
        this.nickname = comment.getMemberId().getNickname();
        this.created_at = comment.getCreatedAt();
    }
}
