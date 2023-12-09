package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.article.AddArticleRequest;
import dev.challduck.portfolio.dto.article.ArticleResponse;
import dev.challduck.portfolio.dto.article.UpdateArticleRequest;
import dev.challduck.portfolio.service.ArticleService;
import dev.challduck.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleApiController {
    private final ArticleService articleService;
    private final MemberService memberService;

    // 새로운 게시글 작성
    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, @AuthenticationPrincipal UserDetails userDetails){
        Member member = memberService.getMemberByEmail(userDetails.getUsername());

        Article savedArticle = articleService.save(request, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    // 게시글 목록을 모두 조회한다.
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticle(){
        List<ArticleResponse> articles = articleService.findAllByOrderByArticleIdDesc()
                .stream()
                .map((response)-> {
                    Member member = response.getMember();
                    String nickname = (member != null) ? member.getNickname() : null;
                    return new ArticleResponse(response, nickname);
                })
                .toList();
        return ResponseEntity.ok()
                .body(articles);
    }

    // 게시글의 상세 내용을 조회한다.
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id){
        Article article = articleService.findById(id);
        Member member = memberService.getMemberById(article.getMember().getId());
        articleService.increaseHitCount(member.getLastLoginIp(), id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article, member.getNickname()));
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle = articleService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        articleService.delete(id);
        return ResponseEntity.ok().build();

    }

}
