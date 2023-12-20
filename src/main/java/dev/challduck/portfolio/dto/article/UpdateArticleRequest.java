package dev.challduck.portfolio.dto.article;

import dev.challduck.portfolio.domain.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {
    @Schema(description = "수정할 게시글 제목", example = "게시글 제목을 수정합니다.")
    private String title;
    @Schema(description = "수정할 게시글 내용", example = "게시글 내용을 수정합니다.")
    private String content;

}
