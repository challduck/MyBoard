package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.dto.article.ArticleLikeViewResponse;
import dev.challduck.portfolio.service.ArticleLikeService;
import dev.challduck.portfolio.service.ArticleService;
import dev.challduck.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleLikeApiController {
    private final ArticleLikeService articleLikeService;
    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping("/articles/{id}/like")
    public ResponseEntity<ArticleLikeViewResponse> getArticleLike(@PathVariable Long id){
        ArticleLikeViewResponse response = articleLikeService.getArticleLike(articleService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/articles/{id}/like")
    public ResponseEntity<Void> articleLike(@PathVariable Long id){

        articleLikeService.save(
                articleService.findById(id),
                memberService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
