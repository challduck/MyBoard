package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Comment;
import lombok.Getter;

@Getter
public class UpdateCommentResponse {
    private final String body;
    public UpdateCommentResponse(Comment comment){
        this.body = comment.getBody();
    }
}
