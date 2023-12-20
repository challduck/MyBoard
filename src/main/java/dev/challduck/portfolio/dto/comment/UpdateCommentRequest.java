package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Optional;

@Getter
public class UpdateCommentRequest {
    @Schema(description = "댓글 수정 내용", example = "댓글을 수정합니다.")
    private String body;
}
