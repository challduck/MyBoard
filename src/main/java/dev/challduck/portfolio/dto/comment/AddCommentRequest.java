package dev.challduck.portfolio.dto.comment;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddCommentRequest {
    private Member member;
    private Article article;
    @Schema(description = "댓글 내용", example = "새로운 댓글을 작성합니다.")
    private String body;
}
