package dev.challduck.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.challduck.portfolio.config.jwt.JwtFactory;
import dev.challduck.portfolio.config.jwt.JwtProperties;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.RefreshToken;
import dev.challduck.portfolio.dto.token.CreateAccessTokenRequest;
import dev.challduck.portfolio.repository.MemberRepository;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        memberRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception{
        // given
        final String url = "/api/token";

        Member testMember = memberRepository.save(Member.builder()
                .email("user@gmail.com")
                .password("test")
                .nickname("testMember")
                .build());

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testMember.getId()))
                .build()
                .createToken(jwtProperties);

        log.info("testMember id: {}", testMember.getId());
        log.info("refreshToken : {}", refreshToken);

        refreshTokenRepository.save(new RefreshToken(testMember ,refreshToken));
        log.info("tokenRepository : {}", refreshTokenRepository.findByRefreshToken(refreshToken).get().getRefreshToken());

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);

        final String requestBody = objectMapper.writeValueAsString(request);
        log.info("requestBody : {}", requestBody);
        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
        log.info("resultAction : {}", resultActions.andExpect(status().isCreated()));
        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
        log.info("accessToken : {}", jsonPath("$.accessToken"));
    }

}