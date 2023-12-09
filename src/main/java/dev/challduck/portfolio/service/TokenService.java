package dev.challduck.portfolio.service;

import dev.challduck.portfolio.config.jwt.TokenProvider;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    public String createNewAccessToken(String refreshToken){
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected member");
        }
        Long memberId = refreshTokenService.processRefreshToken(refreshToken);
//        log.info("getMemberId firstMethod : {}", refreshTokenService.findByRefreshToken(refreshToken).getMember());
//        log.info("getMemberId secondMethod : {}", refreshTokenService.findByRefreshToken(refreshToken).getMember().getId());
        Member member = memberService.findByMemberId(memberId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}
