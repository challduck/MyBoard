package dev.challduck.portfolio.dto.article;

import lombok.Getter;

@Getter
public class ArticleLikeViewResponse {
    private final Long articleLike;

    public ArticleLikeViewResponse(Long articleLike){
        this.articleLike = articleLike;
    }
}
