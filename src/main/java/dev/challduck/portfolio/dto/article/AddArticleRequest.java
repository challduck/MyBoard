package dev.challduck.portfolio.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddArticleRequest {
    @Schema(description = "게시글 제목", example = "새로운 게시글을 작성합니다.")
    private String title;
    @Schema(description = "게시글 내용", example = "새로운 게시글의 내용을 작성합니다.")
    private String content;
}
