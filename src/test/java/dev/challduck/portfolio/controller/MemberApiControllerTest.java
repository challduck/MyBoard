package dev.challduck.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.challduck.portfolio.config.jwt.JwtFactory;
import dev.challduck.portfolio.config.jwt.JwtProperties;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.RefreshToken;
import dev.challduck.portfolio.dto.member.SignInMemberRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MemberApiControllerTest {

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

    @Autowired
    BCryptPasswordEncoder encoder;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
//        memberRepository.deleteAll();
    }

    @DisplayName("validToken: 토큰의 유효성을 검증한다.")
    @Test
    void signIn() throws Exception{

        // given
        // 테스트 유저를 생성하고 jjwt 라이브러리를 이용하여 리프레시 토큰을 만들어 DB에 저장한다.
        // 토큰 생성 API의 요청 본문에 리프레시 토큰ㅇ르 포함하여 요청 객체를 생성한다.
        final String url = "/api/user/signin";

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

        log.info("token : "+ refreshTokenRepository.findByRefreshToken(refreshToken).get().getRefreshToken());

        final String requestBody = objectMapper.writeValueAsString(request);

        log.info("request : "+ requestBody);
        log.info("repository email : "+ memberRepository.findByEmail(member.getEmail()).get().getEmail());
        log.info("repository password : "+ memberRepository.findByEmail(member.getEmail()).get().getPassword());
        log.info("member password : " + member.getPassword());
        log.info("repository password : "+ encoder.matches("test", memberRepository.findByEmail(member.getEmail()).get().getPassword()));

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
        log.info("resultAction : {}",mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody)));
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().string("Authorization", not(emptyString())))
                .andExpect(header().string("Authorization", startsWith("Bearer ")));
//                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }
}