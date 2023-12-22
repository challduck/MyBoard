package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.article.AddArticleRequest;
import dev.challduck.portfolio.dto.article.ArticleResponse;
import dev.challduck.portfolio.dto.article.ArticleViewResponse;
import dev.challduck.portfolio.dto.article.UpdateArticleRequest;
import dev.challduck.portfolio.service.ArticleService;
import dev.challduck.portfolio.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Article", description = "Article Api docs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleApiController {
    private final ArticleService articleService;
    private final MemberService memberService;


    @Operation(summary = "게시글 생성", description = "게시글을 생성 합니다.")
    @ApiResponse(responseCode = "201",description = "게시글 생성 성공하였습니다.",
        headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(
            @RequestBody AddArticleRequest request){

        Member member = memberService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Article savedArticle = articleService.save(request, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200",description = "게시글 조회 성공하였습니다.")
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

    @Operation(summary = "게시글 상세 조회", description = "게시글을 상세 조회합니다.")
    @ApiResponse(responseCode = "200",description = "게시글 상세 조회 성공하였습니다.", content = @Content(schema = @Schema(implementation = ArticleViewResponse.class)))
    @ApiResponse(responseCode = "400",description = "조회할 게시글이 존재하지 않음.")
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleViewResponse> findArticle(
            @PathVariable @Parameter(description = "조회할 게시글 Id", example = "1") Long id,
            @RequestHeader(value = "X-Forwarded-For", defaultValue = "") String xForwardedFor){
        try{
            Article article = articleService.findById(id);
            String clientIp = xForwardedFor != null ? xForwardedFor.split(",")[0].trim() : "Unknown";
            articleService.increaseHitCount(clientIp, id);
            return ResponseEntity.ok().body(new ArticleViewResponse(article));

        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponse(responseCode = "200",description = "게시글 수정을 성공하였습니다.",
            headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "수정할 게시글이 존재하지 않음.")
    @ApiResponse(responseCode = "401",description = "본인이 작성한 게시글만 수정 가능합니다.")
    @PutMapping("/articles/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable @Parameter(description = "수정할 게시글 Id", example = "1") Long id, @RequestBody UpdateArticleRequest request){
        articleService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponse(responseCode = "200",description = "게시글 삭제를 성공하였습니다.",
            headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "삭제할 게시글이 존재하지 않음.")
    @ApiResponse(responseCode = "401",description = "본인이 작성한 게시글만 삭제 가능합니다.")
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable @Parameter(description = "삭제할 게시글 Id", example = "1") Long id){
        articleService.delete(id);
        return ResponseEntity.ok().build();

    }

}
