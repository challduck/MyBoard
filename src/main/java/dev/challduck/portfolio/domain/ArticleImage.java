package dev.challduck.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_image_id", updatable = false)
    private Long articleImageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article articleId;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_format")
    private String imageFormat;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
