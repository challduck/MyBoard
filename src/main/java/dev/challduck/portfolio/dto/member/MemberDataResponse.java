package dev.challduck.portfolio.dto.member;

import dev.challduck.portfolio.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDataResponse {
    @Schema(description = "이메일", example = "newmember@naver.com")
    private final String email;
    @Schema(description = "닉네임", example = "bbs_member")
    private final String nickname;
    private final LocalDateTime createdAt;
    @Schema(description = "OAuth사용자", example = "false")
    private final boolean isOauth;

    public MemberDataResponse(Member member, boolean isOauth){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
        this.isOauth = isOauth;
    }
}
