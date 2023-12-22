package dev.challduck.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.challduck.portfolio.config.jwt.JwtFactory;
import dev.challduck.portfolio.config.jwt.JwtProperties;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.RefreshToken;
import dev.challduck.portfolio.dto.article.AddArticleRequest;
import dev.challduck.portfolio.dto.member.SignInMemberRequest;
import dev.challduck.portfolio.repository.ArticleRepository;
import dev.challduck.portfolio.repository.MemberRepository;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ArticleApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    private String performLogin() throws Exception{
        final String url = "/api/user/login";

        Member member = memberRepository.save(Member.builder()
                .email("user@gmail.com")
                .password(encoder.encode("test"))
                .nickname("testUser")
                .build());

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", member.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(member, refreshToken));

        SignInMemberRequest request = new SignInMemberRequest();
        request.setEmail(member.getEmail());
        request.setPassword("test");

        final String requestBody = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        return resultActions.andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    @DisplayName("새로운 Article 작성에 성공한다.")
    void addArticle() throws Exception {
        String token = performLogin();
        log.info("token value : {}", token);
        final String articleTitle = "게시글의 제목";
        final String articleContent = "게시글의 내용";
        final String url = "/api/articles";

        AddArticleRequest request = new AddArticleRequest();
        request.setTitle(articleTitle);
        request.setContent(articleContent);
        final String requestBody = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(post(url)
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        resultActions
                .andExpect(status().isCreated());
    }

}