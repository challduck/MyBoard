package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.article.AddArticleRequest;
import dev.challduck.portfolio.dto.article.UpdateArticleRequest;
import dev.challduck.portfolio.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final Map<String, Long> ipLastAccessMap = new ConcurrentHashMap<>();
    private static final long ACCESS_INTERVAL = 5 * 60 * 1000; // 5분
    public List<Article> findAllByOrderByArticleIdDesc() {
        return articleRepository.findAllByOrderByArticleIdDesc();
    }

    public Article save(AddArticleRequest request, Member member) {
        return articleRepository.save(Article.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .member(member)
                .build());
    }

    // 게시글 상세 조회
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: "+id));
    }

    // 게시글 조회수 증가 메서드
    @Transactional
    public void increaseHitCount(String clientIp, Long articleId){
        if (!isDuplicateAccess(clientIp)){
            Article article = articleRepository.findById(articleId).orElse(null);
            if (article != null){
                article.setHitCount(article.getHitCount() + 1);
                articleRepository.save(article);
                updateLastAccess(clientIp);
            }
        }
    }

    // article 상세페이지 중복 접근 확인 메서드
    private boolean isDuplicateAccess(String clientIp){
        Long lastAccess = ipLastAccessMap.get(clientIp);

        if(lastAccess != null){
            long currentTile = System.currentTimeMillis();
            return (currentTile - lastAccess) < ACCESS_INTERVAL;
        }
        return false;
    }

    private void updateLastAccess(String clientIp){
        ipLastAccessMap.put(clientIp, System.currentTimeMillis());
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found : "+ id));
        authorizeArticleAuthor(article);
        article.update(request);
        articleRepository.save(article);
        return article;
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found : "+ id));
        authorizeArticleAuthor(article);
        articleRepository.delete(article);
    }

    // 게시글을 작성한 사용자인지 확인하는 메서드
    private static void authorizeArticleAuthor(Article article){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        // member 객체를 Member Entity로 전달하여 userName을 가져오는 로직
        if(!article.getMember().getEmail().equals(userName)){
            throw new IllegalArgumentException("not authorized");
        }
    }
}
