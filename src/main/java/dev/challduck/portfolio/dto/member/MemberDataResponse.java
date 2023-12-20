package dev.challduck.portfolio.dto.member;

import dev.challduck.portfolio.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDataResponse {
    private final String email;
    private final String nickname;
    private final LocalDateTime createdAt;
    private final boolean isOauth;

    public MemberDataResponse(Member member, boolean isOauth){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
        this.isOauth = isOauth;
    }
}
