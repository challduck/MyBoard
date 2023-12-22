package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.dto.comment.*;
import dev.challduck.portfolio.service.ArticleService;
import dev.challduck.portfolio.service.CommentService;
import dev.challduck.portfolio.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment", description = "Comment Api docs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentApiController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final MemberService memberService;


    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200",description = "댓글 목록 조회 성공하였습니다.")
    @ApiResponse(responseCode = "400",description = "가져올 댓글의 목록이 존재하지않습니다.")
    @GetMapping("/articles/{id}/comments")
    public ResponseEntity<List<CommentViewResponse>> comments(@PathVariable @Parameter(description = "댓글을 조회할 게시글 Id", example = "4") Long id){
        Article article = articleService.findById(id);
        List<CommentViewResponse> commentViewResponse = commentService.comments(article);

        return ResponseEntity.status(HttpStatus.OK).body(commentViewResponse);
    }

    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @ApiResponse(responseCode = "200",description = "댓글 작성을 성공하였습니다.",
        headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "댓글을 작성할 게시글이 존재하지않습니다.")
    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<AddCommentResponse> create(
            @PathVariable @Parameter(description = "댓글을 작성할 게시글 Id", example = "4") Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddCommentRequest request){

        Comment comment = commentService.save(
                articleService.findById(id),
                memberService.findByEmail(userDetails.getUsername()),
                request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AddCommentResponse(comment));
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponse(responseCode = "200",description = "댓글 수정을 성공하였습니다.",
        headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "댓글을 수정할 게시글이 존재하지않습니다.")
    @ApiResponse(responseCode = "401",description = "본인이 작성한 댓글만 수정 가능합니다.")
    @PutMapping("/comments/{id}")
    public ResponseEntity<UpdateCommentResponse> update(@PathVariable @Parameter(description = "댓글을 수정할 댓글 Id", example = "10") Long id,
                                                        @RequestBody UpdateCommentRequest request){
        UpdateCommentResponse update =  commentService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponse(responseCode = "200",description = "댓글 수정을 성공하였습니다.",
        headers = {@Header(name = "Authorization", description = "Bearer access_token", required = true)})
    @ApiResponse(responseCode = "400",description = "댓글을 수정할 게시글이 존재하지않습니다.")
    @ApiResponse(responseCode = "401",description = "본인이 작성한 댓글만 삭제 가능합니다.")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "댓글을 삭제할 댓글 Id", example = "10") Long id){
        commentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
