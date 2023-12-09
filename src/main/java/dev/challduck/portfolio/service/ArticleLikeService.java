package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.ArticleLike;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.article.ArticleLikeViewResponse;
import dev.challduck.portfolio.repository.ArticleLikeRepository;
import dev.challduck.portfolio.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;

    public ArticleLikeViewResponse getArticleLike(Article article){
        return new ArticleLikeViewResponse(articleLikeRepository.countByArticleId(article));
    }

    @Transactional
    public void save(Article article, Member member) {
        if (!articleLikeRepository.existsByMemberIdAndArticleId(member, article)){
            articleLikeRepository.save(new ArticleLike(member,article));
        }
        else {
            throw new IllegalArgumentException("게시글의 추천은 한번만 가능합니다.");
        }
    }


}
