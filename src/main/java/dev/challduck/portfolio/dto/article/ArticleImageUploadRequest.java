package dev.challduck.portfolio.dto.article;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.ArticleImage;
import lombok.Getter;

@Getter
public class ArticleImageUploadRequest {
    private Article articleId;
    private String imageName;
    private String imageFormat;

//    public ArticleImageUploadRequest(Article article, String imageName, String imageFormat){
//        this.articleId = article;
//        this.imageName = imageName;
//        this.imageFormat = imageFormat;
//    }

    public ArticleImage toEntity(Article article, String imageName, String imageFormat){
        return ArticleImage.builder()
                .article(article)
                .imageFormat(imageFormat)
                .imageName(imageName)
                .build();
    }
}
