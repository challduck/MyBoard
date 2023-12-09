package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Comment;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.comment.CommentViewResponse;
import dev.challduck.portfolio.dto.comment.AddCommentRequest;
import dev.challduck.portfolio.dto.comment.UpdateCommentRequest;
import dev.challduck.portfolio.dto.comment.UpdateCommentResponse;
import dev.challduck.portfolio.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;


    public List<CommentViewResponse> comments(Article article){
        return commentRepository
                .findByArticleId(article)
                .stream()
                .map(comment -> new CommentViewResponse(comment, comment.getMemberId()))
                .toList();
    }

    @Transactional
    public Comment save(Article article, Member member, AddCommentRequest request) {
        return commentRepository.save(request.toEntity(member, article));
    }

    @Transactional
    public UpdateCommentResponse update(Long id, UpdateCommentRequest request){
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Not Found " + id));
        authorizeCommentAuthor(comment);
        comment.update(request);
        return new UpdateCommentResponse(commentRepository.save(comment));
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Not Found : " + id));
        authorizeCommentAuthor(comment);
        commentRepository.delete(comment);
    }

    // 댓글을 작성한 사용자인지 확인하는 메서드
    private static void authorizeCommentAuthor(Comment comment){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        // member 객체를 Member Entity로 전달하여 userName을 가져오는 로직
        if(!comment.getArticleId().getMember().getEmail().equals(userName)){
            throw new IllegalArgumentException("Not Authorized");
        }
    }

}
