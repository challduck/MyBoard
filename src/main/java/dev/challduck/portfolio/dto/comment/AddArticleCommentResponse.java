package dev.challduck.portfolio.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddArticleCommentResponse {
    private String nickname;
    private String body;
}
