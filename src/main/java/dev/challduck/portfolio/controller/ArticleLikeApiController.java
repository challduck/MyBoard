package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.dto.article.ArticleLikeViewResponse;
import dev.challduck.portfolio.service.ArticleLikeService;
import dev.challduck.portfolio.service.ArticleService;
import dev.challduck.portfolio.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Article Like", description = "Article Like Api docs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleLikeApiController {
    private final ArticleLikeService articleLikeService;
    private final ArticleService articleService;
    private final MemberService memberService;

    @Operation(summary = "게시글 추천 조회", description = "게시글의 추천을 조회합니다.")
    @ApiResponse(responseCode = "200",description = "게시글의 추천수 조회를 성공하였습니다.")
    @ApiResponse(responseCode = "400",description = "추천수를 가져올 게시글이 존재하지않습니다.")
    @GetMapping("/articles/{id}/like")
    public ResponseEntity<ArticleLikeViewResponse> getArticleLike(@PathVariable @Parameter(description = "추천수를 조회할 게시글 Id", example = "1") Long id){
        ArticleLikeViewResponse response = articleLikeService.getArticleLike(articleService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "게시글 추천", description = "게시글을 추천합니다.")
    @ApiResponse(responseCode = "200",description = "게시글 추천을 성공하였습니다.",
            headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "추천할 게시글이 존재하지않습니다.")
    @ApiResponse(responseCode = "401",description = "로그인한 회원만 게시글 추천이 가능합니다.")
    @ApiResponse(responseCode = "409",description = "게시글 추천은 한번만 가능 합니다.")
    @PostMapping("/articles/{id}/like")
    public ResponseEntity<Void> articleLike(@PathVariable @Parameter(description = "추천할 게시글 Id", example = "1") Long id){
        try {
            articleLikeService.save(
                    articleService.findById(id),
                    memberService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
}
