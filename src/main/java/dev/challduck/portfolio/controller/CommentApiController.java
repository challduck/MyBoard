package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.dto.comment.*;
import dev.challduck.portfolio.service.ArticleService;
import dev.challduck.portfolio.service.CommentService;
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
public class CommentApiController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final MemberService memberService;

    // comment 조회
    @GetMapping("/articles/{id}/comments")
    public ResponseEntity<List<CommentViewResponse>> comments(@PathVariable Long id){
        Article article = articleService.findById(id);
        List<CommentViewResponse> commentViewResponse = commentService.comments(article);

        return ResponseEntity.status(HttpStatus.OK).body(commentViewResponse);
    }

    // comment 작성
    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<AddCommentResponse> create(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddCommentRequest request){

        Comment comment = commentService.save(
                articleService.findById(id),
                memberService.getMemberByEmail(userDetails.getUsername()),
                request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AddCommentResponse(comment));
    }

    // comment 수정
    @PutMapping("/comments/{id}")
    public ResponseEntity<UpdateCommentResponse> update(@PathVariable Long id,
                                                        @RequestBody UpdateCommentRequest request){
        UpdateCommentResponse update =  commentService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    // comment 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        commentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
