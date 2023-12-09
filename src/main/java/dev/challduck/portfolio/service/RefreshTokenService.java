package dev.challduck.portfolio.service;

import dev.challduck.portfolio.config.jwt.TokenProvider;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.RefreshToken;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected member"));
    }

    /*
     * refreshToken을 전달받아 이를 MemberId로 변환하는 서비스 로직
     */
    public Long processRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(existingRefreshToken -> existingRefreshToken.getMember().getId())
                .orElseThrow(()-> new IllegalArgumentException("Unexpected member"));
    }

    // 로그인 성공시 refreshToken 발급
    public String loginSuccessMemberGenerateRefreshToken(Member member){
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member, refreshToken);
        return refreshToken;
    }
    // 로그인 성공시 accessToken 발급
    public String loginSuccessMemberGenerateAccessToken(Member member){
        return tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
    }

    // Member 객체와 새로운 RefreshToken을 받아온다.
    // update 하는데 안되면 refreshToken을 새로 만들어서 변수에 저장한다.
    public void saveRefreshToken(Member member, String newRefreshToken){
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(member, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }
}
