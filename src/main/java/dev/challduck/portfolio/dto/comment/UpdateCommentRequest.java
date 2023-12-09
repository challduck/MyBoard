package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Comment;
import lombok.Getter;

import java.util.Optional;

@Getter
public class UpdateCommentRequest {
    private String body;
}
