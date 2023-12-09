package dev.challduck.portfolio.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ArticleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_image_id", updatable = false)
    private Long articleImageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    @NotNull
    private Article articleId;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    @NotNull
//    private Member memberId;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_format")
    private String imageFormat;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public ArticleImage(Article article, String imageName, String imageFormat){
        this.articleId = article;
        this.imageName = imageName;
        this.imageFormat = imageFormat;
    }
}
